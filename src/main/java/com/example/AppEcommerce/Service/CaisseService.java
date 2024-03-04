package com.example.AppEcommerce.Service;

import com.example.AppEcommerce.Dto.CaisseDto;
import com.example.AppEcommerce.Dto.getArticleCaisseDto;
import com.example.AppEcommerce.Enum.Role;
import com.example.AppEcommerce.Enum.Status;
import com.example.AppEcommerce.Impl.CaisseServiceImp;
import com.example.AppEcommerce.Model.*;

import com.example.AppEcommerce.Repository.*;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;

@Service
public class CaisseService implements CaisseServiceImp {

    @Autowired
    private CaisseRepository caisseRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RevenueDateRepository revenueDateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PagesRepository pageRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
   private BenifitsVendorRepository benifitsVendorRepository;
    @Autowired
    private ArticleCaisseRepository articleCaisseRepository;
    @Override
    public String addCaisse(CaisseDto caisseDto) throws FirebaseMessagingException {
        User user = userService.getUserByPage(caisseDto.getIdPage());
        Caisse caisse = new Caisse(caisseDto.getIdSender(), caisseDto.getAddress(), caisseDto.getStreetAddress(), caisseDto.getPhone(), caisseDto.getSelectedTime(), caisseDto.getDescription(), user.getId(), caisseDto.getSubTotal(),  caisseDto.getArticles(), LocalDate.now());
        Caisse caisse2 = caisseRepository.save(caisse);
        sendCaisseNotif(caisse2.getId());
        return caisse2.getId();
    }

    @Override
    public void sendCaisseNotif(String id) throws FirebaseMessagingException {
        Caisse caisse = caisseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("caisse not found with ID" + id));
        notificationService.sendVendorPushNotification(caisse.getId());
        notificationService.sendDeliveriesPushNotificationLocal(caisse.getId());
    }

    @Override
    public Caisse getCaisseById(String id) {
        return caisseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Caisse not found with id " + id));
    }

    @Override
    public List<getArticleCaisseDto> getArticlesCaisse(List<ArticleCaisse> articleCaisses) {
        List<getArticleCaisseDto> getArticleCaisseDtos = new ArrayList<>();
        articleCaisses.forEach(articleCaisse -> {
            Article article = articleRepository.findById(articleCaisse.getIdArticle())
                    .orElseThrow(() -> new NoSuchElementException("Page not found with id " + articleCaisse.getIdArticle()));
            getArticleCaisseDto getArticleCaisseDto = new getArticleCaisseDto(article.getNom(), article.getPrix(), articleCaisse.getQnt(), article.getImage(), article.getPage());
            getArticleCaisseDtos.add(getArticleCaisseDto);
        });
        return getArticleCaisseDtos;
    }

    @Override
    public void addDeliveryToCaisse(String idCaisse, String idDelivery) throws FirebaseMessagingException {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setIdDelivery(idDelivery);
        caisseRepository.save(caisse);
        notificationService.sendNotificationFromDeliveryToVendor(caisse.getId());
    }

    @Override
    public List<Caisse> caisseListDelivery(String idDelivery) {
        return caisseRepository.findByidDelivery(idDelivery);
    }

    @Override
    public void CancelByVendor(String idCaisse) throws FirebaseMessagingException {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.ANNULE);
        caisseRepository.save(caisse);
        notificationService.sendCancelNotificationByVendor(idCaisse);
    }

    @Override
    public void AcceptByVendor(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.EN_PREPARATION);
        caisseRepository.save(caisse);
    }

    @Override
    public List<Caisse> caisseListClient(String idSender) {
        return caisseRepository.findByidSender(idSender);
    }

    @Override
    public List<Caisse> caisseListVendor(String idVendor) {
        List<Caisse> newCaisse = new ArrayList<>();
        caisseRepository.findByidVendor(idVendor).forEach(caisse -> {
            if (caisse.getStatus() != null) {
                newCaisse.add(caisse);
            }
        });
        return newCaisse;
    }

    @Override
    public void CancelByClient(String idCaisse) throws FirebaseMessagingException {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.ANNULE);
        caisseRepository.save(caisse);
        notificationService.sendCancelNotificationByClient(idCaisse);
    }

    @Override
    public void SetStatutBeingdelivred(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.EN_COURS_DE_LIVRAISON);
        caisseRepository.save(caisse);
    }

    @Override
    public void setStatutDelivered(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.LIVRE);
        caisseRepository.save(caisse);

        User delivery = userRepository.findById(caisse.getIdDelivery())
                .orElseThrow(() -> new NoSuchElementException("User not found with ID " + caisse.getIdDelivery()));

        double fraisTotal = fraisTotal(caisse.getIdDelivery());
        double commission = comissionFrais(fraisTotal);

        List<RevenueDate> revenueDates = delivery.getRevenueDates();
        boolean dateFound = false;

        for (RevenueDate revenueDate : revenueDates) {
            if (revenueDate.getDate().equals(caisse.getDate())) {
                double newRevenue = formatDecimal(fraisTotal - commission); // Calculate new revenue
                revenueDate.setRevenue(newRevenue);
                revenueDateRepository.save(revenueDate);
                dateFound = true;
                break;
            }
        }

        if (!dateFound) {
            double newRevenue = formatDecimal(fraisTotal - commission); // Calculate new revenue
            RevenueDate revenueDate1 = new RevenueDate(newRevenue, caisse.getDate());
            revenueDateRepository.save(revenueDate1);
            delivery.getRevenueDates().add(revenueDate1);
        }

        userRepository.save(delivery);
    }


    @Override
    public double fraisDelivery(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        User client = userRepository.findById(caisse.getIdSender())
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + caisse.getIdSender()));

        double clientLat = client.getLatitude();
        double clientLong = client.getLongitude();
        List<ArticleCaisse> articleCaisseList = caisse.getArticles();
        ArticleCaisse articleCaisse = articleCaisseList.get(0);
        String articleId = articleCaisse.getIdArticle();
        Optional<Article> article = articleRepository.findById(articleId);
        Optional<Pages> page = article.map(a -> pageRepository.findById(a.getPage().getId()))
                .orElseThrow(() -> new NoSuchElementException("Page not found"));
        double pageLat = page.get().getLatitude();
        double pageLong = page.get().getLongitude();
        double frais = 0.0;
        // Calculating the distance between client and page using the Haversine formula
        double distance = calculateDistance(clientLat, clientLong, pageLat, pageLong);

        // Calculating the frais based on the distance
        if (distance <= 7) {
            frais = distance * 1; // Add 1d for each kilometer
        }
        return formatDecimal(frais);
    }

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in kilometers
        double earthRadius = 6371;

        // Convert latitude and longitude to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences between the latitudes and longitudes
        double latDiff = lat2Rad - lat1Rad;
        double lonDiff = lon2Rad - lon1Rad;

        // Calculate the distance using the Haversine formula
        double a = Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(lonDiff / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
    public double fraisTotal(String idDelivery){
        List<Caisse> caisses = caisseRepository.findByidDelivery(idDelivery);
        List<Caisse> caissesDelivered = new ArrayList<>();
        caisses.forEach(caisse -> {
            if(caisse.getStatus().equals(Status.LIVRE)){
                caissesDelivered.add(caisse);
            }
        });
        AtomicDouble totalFrais = new AtomicDouble(0.0); // Use AtomicDouble instead of double

        caissesDelivered.forEach(caisse -> {
            double frais = fraisDelivery(caisse.getId());
            totalFrais.addAndGet(frais); // Update the AtomicDouble using atomic operation
        });

        return formatDecimal(totalFrais.get());
    }
    public double comissionFrais(double totalFrais){
        double commission = totalFrais * 0.1; // Calculate 10% of fraisTotal
        return formatDecimal(commission);
    }
    private double formatDecimal(double value) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);
        return Double.parseDouble(decimalFormat.format(value));
    }

    @Override
    public double calculateTotalCommissionAndFrais(String userId) {
        List<Caisse> caisses = caisseRepository.findByidDelivery(userId);
        Set<String> calculatedVendorIds = new HashSet<>(); // Pour stocker les IDs de vendeurs déjà calculés
        double totalFraisVendeurs = 0.0; // Initialisez la somme totale des frais de vendeurs
        double totalCommissionLivreur = 0.0; // Initialisez la commission totale du livreur

        for (Caisse caisse : caisses) {
            if (caisse.getStatus().equals(Status.LIVRE)) {
                String vendorId = caisse.getIdVendor();

                // Éviter de recalculer les frais pour le même vendeur
                if (!calculatedVendorIds.contains(vendorId)) {
                    calculatedVendorIds.add(vendorId);

                    User vendorUser = userRepository.findById(vendorId)
                            .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + vendorId));

                    double fraisVendeur = calculateTotalFrais(vendorUser.getId()); // Utiliser les frais déjà calculés
                    totalFraisVendeurs += fraisVendeur; // Ajouter les frais du vendeur à la somme totale des frais de vendeurs
                }
            }
        }

        totalCommissionLivreur = comissionFrais(totalFraisVendeurs);

        double totalCommissionAndFrais = totalCommissionLivreur + totalFraisVendeurs;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID" + userId));
        user.setCommissiontotale(formatDecimal(totalCommissionAndFrais));
        userRepository.save(user);
        return formatDecimal(totalCommissionAndFrais);
    }

    @Override
    public LocalDate getDateByIdCaisse(String idCaisse) {
        Caisse caisse = caisseRepository.findById(idCaisse).orElse(null);
        if (caisse != null) {
            return caisse.getDate();
        }
        return null;
    }

    @Override
    public List<Caisse> getAllCaisse() {
        return caisseRepository.findAll();
    }

    @Override
    public int getDeliveredCountForDelivery(String id) {
        List<Caisse> caisses = caisseRepository.findByidDelivery(id);
        int deliveredCount = 0;
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                deliveredCount++;
            }
        }
        return deliveredCount;
    }

    @Override
    public int getCancelCountForDelivery(String id) {
        List<Caisse> caisses = caisseRepository.findByidDelivery(id);
        int cancelCount = 0;
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.ANNULE) {
                cancelCount++;
            }
        }
        return cancelCount;
    }

    @Override
    public int getNbCaisseForDelivery(String id) {
        List<Caisse> caisses = caisseRepository.findByidDelivery(id);
        int caisseCount = 0;
        for (Caisse caisse : caisses) {
            {
                caisseCount++;
            }
        }
        return caisseCount;
    }

    @Override
    public int getNbCaisseForVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        int caisseCount = 0;
        for (Caisse caisse : caisses) {
            {
                caisseCount++;
            }
        }
        return caisseCount;
    }

    @Override
    public double  calculateTotalRevenueForAllCaisse(String id) {
        List<Caisse> caisses = caisseRepository.findByidDelivery(id);
       double totalRevenue = 0.0;

        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                User delivery = userRepository.findById(caisse.getIdDelivery())
                        .orElseThrow(() -> new NoSuchElementException("user not found with ID" + caisse.getIdDelivery()));
                List<RevenueDate> revenueDates = delivery.getRevenueDates();
                for (RevenueDate revenueDate : revenueDates) {
                    totalRevenue += revenueDate.getRevenue();
                }
            }
        }

        return totalRevenue = formatDecimal(totalRevenue);
    }



    @Override
    public int getDeliveredCountForVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        int deliveredCount = 0;
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                deliveredCount++;
            }
        }
        return deliveredCount;
    }

    @Override
    public int countClients(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        Set<String> uniqueIdSenders = new HashSet<>();

        for (Caisse caisse : caisses) {
            if (caisse.getIdSender() != null) {
                uniqueIdSenders.add(caisse.getIdSender());
            }
        }

        return uniqueIdSenders.size();
    }

    @Override
    public int getCancelCountForVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        int cancelCount = 0;
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.ANNULE) {
                cancelCount++;
            }
        }
        return cancelCount;
    }


    private List<Caisse> previousCaisses = null;
    private Map<List<Caisse>, List<BenifitsVendor>> cachedResults = new HashMap<>();

    public void benefitsVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);

        if (!caisses.isEmpty() && !caisses.equals(previousCaisses)) {
            List<BenifitsVendor> benifitsVendors;

            if (cachedResults.containsKey(caisses)) {
                benifitsVendors = cachedResults.get(caisses);
            } else {
                benifitsVendors = calculateBenifits(caisses);
                cachedResults.put(new ArrayList<>(caisses), benifitsVendors);
            }

            updateUserData(id, benifitsVendors);

            // Mettre à jour la référence de la liste des caisses précédemment utilisée
            previousCaisses = new ArrayList<>(caisses);
        }
    }

    private List<BenifitsVendor> calculateBenifits(List<Caisse> caisses) {
        List<BenifitsVendor> calculatedBenifits = new ArrayList<>();

        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                LocalDate caisseDate = caisse.getDate();
                BenifitsVendor benifitsVendor = new BenifitsVendor(0, 0, 0, caisseDate);

                // Calculs des bénéfices
                double newChiffre = benifitsVendor.getChiffre() + caisse.getSubTotal();
                benifitsVendor.setChiffre(newChiffre);

                double newFrais = newChiffre * 0.1;
                benifitsVendor.setFrais(newFrais);

                double newBenefits = newChiffre - newFrais;
                benifitsVendor.setBenefits(newBenefits);

                calculatedBenifits.add(benifitsVendor);
            }
        }

        return calculatedBenifits;
    }

    private void updateUserData(String id, List<BenifitsVendor> benifitsVendors) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        // Mise à jour des données de l'utilisateur avec les nouveaux bénéfices
        List<BenifitsVendor> userBenifitsVendors = user.getBenifitsVendors();
        Map<LocalDate, BenifitsVendor> benifitsVendorMap = new HashMap<>();

        // Mise en correspondance des bénéfices existants avec les dates
        for (BenifitsVendor userBenifitsVendor : userBenifitsVendors) {
            benifitsVendorMap.put(userBenifitsVendor.getDate(), userBenifitsVendor);
        }

        for (BenifitsVendor calculatedBenifitsVendor : benifitsVendors) {
            LocalDate benifitsDate = calculatedBenifitsVendor.getDate();
            BenifitsVendor existingBenifitsVendor = benifitsVendorMap.get(benifitsDate);

            if (existingBenifitsVendor != null) {
                // Mettre à jour les données existantes
                existingBenifitsVendor.setChiffre(calculatedBenifitsVendor.getChiffre());
                existingBenifitsVendor.setFrais(calculatedBenifitsVendor.getFrais());
                existingBenifitsVendor.setBenefits(calculatedBenifitsVendor.getBenefits());
            } else {
                benifitsVendorMap.put(benifitsDate, calculatedBenifitsVendor);
            }
        }

        user.setBenifitsVendors(new ArrayList<>(benifitsVendorMap.values()));
        userRepository.save(user);
    }



    public double calculateTotalRevenue(String id) {
        double totalRevenue = 0.0;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));
        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();
        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            totalRevenue += benifitsVendor.getChiffre();
        }
        return totalRevenue = formatDecimal(totalRevenue);
    }

    public double calculateTotalFrais(String id) {
        double totalFrais = 0.0;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();

        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            totalFrais += benifitsVendor.getFrais();
        }

        return totalFrais = formatDecimal(totalFrais);
    }

    public double calculateTotalbenifits(String id) {
        double totalbenifits = 0;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();

        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            totalbenifits+= benifitsVendor.getBenefits();
        }

        return totalbenifits =formatDecimal(totalbenifits);
    }
    @Override
    public List<User> getListClientByVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        List<User> users = new ArrayList<>();
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                User user = userRepository.findById(caisse.getIdSender()).orElseThrow(() -> new NoSuchElementException("user not found with ID" + caisse.getIdSender()));
                users.add(user);
            }
        }
        return users;
    }
    @Override
    public String addAvis(String idArticle, String idSender, String avis) {
        List<Caisse> caisses = caisseRepository.findAll();
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                List<ArticleCaisse> articles = caisse.getArticles();
                for (ArticleCaisse articleCaisse : articles) {
                    if (articleCaisse.getIdArticle().equals(idArticle)) {
                        Article article = articleRepository.findById(idArticle)
                                .orElseThrow(() -> new NoSuchElementException("Article not found with ID " + idArticle));
                        if (articleCaisse.getAvis() == null || articleCaisse.getAvis().isEmpty()) {
                            articleCaisse.setAvis(avis);
                            articleCaisseRepository.save(articleCaisse);
                            caisseRepository.save(caisse); // Enregistrer la caisse modifiée
                            return articleCaisse.getIdArticle();
                        } else {
                            // Vérification de l'avis uniquement pour les articles de la même caisse
                            if (caisse.getIdSender().equals(idSender)) {
                                throw new IllegalArgumentException("An existing review already exists for the article.");
                            }
                        }
                    }
                }
            }
        }
        throw new NoSuchElementException("ArticleCaisse not found with ID " + idArticle);
    }
    @Override
    public List<String> getAvisParArticle(String idArticle) {
        List<Caisse> caisses = caisseRepository.findAll();
        List<String> avisList = new ArrayList<>();

        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                List<ArticleCaisse> articles = caisse.getArticles();

                for (ArticleCaisse articleCaisse : articles) {
                    if (articleCaisse.getIdArticle().equals(idArticle)) {
                        String avis = articleCaisse.getAvis();

                        if (avis != null) {
                            avisList.add(avis);
                        }
                    }
                }
            }
        }

        return avisList;
    }

    @Override
    public List<String> getListPageParCommande(String id) {
        List<Caisse> caisses = caisseRepository.findByidSender(id);
        List<String> titles = new ArrayList<>(); // Liste pour stocker les titres

        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.LIVRE) {
                List<ArticleCaisse> articleCaisseList = caisse.getArticles();
                if (!articleCaisseList.isEmpty()) {
                    ArticleCaisse articleCaisse = articleCaisseList.get(0); // Par exemple, prenez le premier article
                    String articleId = articleCaisse.getIdArticle();
                    Optional<Article> article = articleRepository.findById(articleId);
                    Optional<Pages> page = article.flatMap(a -> Optional.ofNullable(a.getPage()));

                    if (page.isPresent()) {
                        String title = page.get().getTitle();
                        titles.add(title); // Ajout du titre à la liste
                    }
                }
            }
        }

        return titles;
    }


    //Admin-------------------------------------------------------------------------------------------------------------------------------

    //update solde pour sous admin
    @Override
    public void UpdateSoldeSousAdmin(String id, String id2, int i){
        User u=userRepository.findById(id).orElseThrow();
        User u2=userRepository.findById(id2).orElseThrow();
        //Pour charger tous les soldes ajouter pour le sous admin
        u2.setCompteurC(u2.getCompteurC()+i);
        u2.setT(u2.getT()+i*0.1);
        userRepository.save(u);
        userRepository.save(u2);

    }
    //update solde pour sous admin (carger le page)
    @Override
    public void PageUpdateSolde(String id, String id2, int i){
        User u=userService.getUserByPage(id);
        User u2=userRepository.findById(id2).orElseThrow();
        if(u!=null){
            //Pour charger tous les soldes ajouter pour le sous admin
            u2.setCompteurC(u2.getCompteurC()+i);
            u2.setT(u2.getT()+i*0.1);
            userRepository.save(u);
            userRepository.save(u2);}

    }
    //reset sous admin
    @Override
    public void Reset(String id){
        User u=userRepository.findById(id).orElseThrow();
        u.setCompteurC(0);
        userRepository.save(u);
    }
    @Override
    public void payer(String id, String ids){
        User u=userRepository.findById(id).orElseThrow();
        User u2=userRepository.findById(ids).orElseThrow();

        //Pour charger tous les soldes ajouter pour le sous admin
        u2.setCompteurC(u2.getCompteurC()+u.getCommissiontotale());

        u.setCommissiontotale(0);
        userRepository.save(u);
        userRepository.save(u2);
    }
    @Override
    public void UpdateEtat(String id){
        User u=userRepository.findById(id).orElseThrow();
        u.setEtat(false);
        userRepository.save(u);
    }
    @Override
    public int todaysales() {
        List<Caisse> c= caisseRepository.findAll();
        int revenue=0;
        int prix=0;
        RevenueDate revenueDate = null;
        List<ArticleCaisse> articleCaisses = null;

        for (Caisse cc: c) {
            if (cc.getDate() != null) {
                if (cc.getStatus() == Status.LIVRE && cc.getDate().equals(LocalDate.now())) {
                    for (ArticleCaisse article : cc.getArticles()) {

                        prix = Integer.parseInt(article.getPrix());
                        int i = prix * article.getQnt();
                        int revenu = i;
                        revenue += revenu;


                    }

                }
            }
            else {return 0;}
        }


        return revenue;
    }
    //total sales
    @Override
    public Double totalsales() {
        List<Caisse> c = caisseRepository.findAll();
        double revenue = 0;
        double prix = 0.0;
        for (Caisse cc : c) {
            if (cc.getStatus() == Status.LIVRE){
                for (ArticleCaisse article : cc.getArticles()) {
                    prix = Double.parseDouble(article.getPrix());
                    revenue += prix * article.getQnt();
                }
            }
        }
        return revenue;
    }
    @Override
    public int SalesARTToday() {
        List<Caisse> c= caisseRepository.findAll();
        int i=0;
        List<ArticleCaisse> articleCaisses = null;
        for (Caisse cc: c) {
            if (cc.getDate() != null) {
                if (cc.getStatus() == Status.LIVRE && cc.getDate().equals(LocalDate.now())) {

                    for (ArticleCaisse article : cc.getArticles()) {

                        i = i + article.getQnt();

                    }
                }
            }
        }
        return i;
    }
    @Override
    public int SalesART() {
        List<Caisse> c= caisseRepository.findAll();
        int i=0;
        List<ArticleCaisse> articleCaisses = null;
        for (Caisse cc: c) {
            if (cc.getStatus() == Status.LIVRE ) {
                for (ArticleCaisse article : cc.getArticles()) {
                    i  = i + article.getQnt();

                }
            }
        }
        return i;
    }
    @Override
    public int revenuLivreurT() {
        List<User> users = userRepository.findAll();
        RevenueDate todayRevenue = null;
        int revenue=0;
        int i=0;

        for (User u : users) {
            if (u.getRole()== Role.DELIVERY) {


                for (RevenueDate revenueDate : u.getRevenueDates()) {
                    if  (revenueDate.getDate()!=null) {
                        if (revenueDate.getDate().equals(LocalDate.now())) {
                            revenue += revenueDate.getRevenue();
                            i++;
                        }
                    }
                }

            }

        }
        return  i;
    }

    //nbre de livraison total
    @Override
    public int revenuLivreur() {
        List<User> users = userRepository.findAll();
        int i=0;
        for (User u : users) {
            if (u.getRole()== Role.DELIVERY) {
                if (u.getRevenueDates() != null) {
                    for (RevenueDate revenueDate : u.getRevenueDates()) {
                        i++;
                    }

                }
            }

        }
        return  i;
    }
    //revenu admin today
    @Override
    public double adminRevenu(){
        double somme=0;
        somme=SalesARTToday()*1000+revenuLivreurT()*500;
        return somme;
    }
    //revenu total du admin


    @Override
    public double AdmeinRedvenuTotal(){
        double somme=0;
        somme=SalesART()*1000+revenuLivreur()*500;
        return somme;
    }
}


















