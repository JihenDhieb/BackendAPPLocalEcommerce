package com.example.AppEcommerce.Service;

import com.example.AppEcommerce.Dto.CaisseDto;
import com.example.AppEcommerce.Dto.getArticleCaisseDto;
import com.example.AppEcommerce.Enum.Status;
import com.example.AppEcommerce.Impl.CaisseServiceImp;
import com.example.AppEcommerce.Model.*;

import com.example.AppEcommerce.Repository.*;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    private ArticleRepository articleRepository;
    @Autowired
   private BenifitsVendorRepository benifitsVendorRepository;
    @Autowired
    private ArticleCaisseRepository articleCaisseRepository;
    @Override
    public String addCaisse(CaisseDto caisseDto) throws FirebaseMessagingException {
        User user = userService.getUserByPage(caisseDto.getIdPage());
        Caisse caisse = new Caisse(caisseDto.getIdSender(), caisseDto.getAddress(), caisseDto.getStreetAddress(), caisseDto.getPhone(), caisseDto.getSelectedTime(), caisseDto.getDescription(), user.getId(), caisseDto.getSubTotal(), caisseDto.getFrais(), caisseDto.getTotalPrice(), caisseDto.getArticles(), LocalDate.now());
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
        caisse.setStatus(Status.CANCEL);
        caisseRepository.save(caisse);
        notificationService.sendCancelNotificationByVendor(idCaisse);
    }

    @Override
    public void AcceptByVendor(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.IN_PREPARATION);
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
        caisse.setStatus(Status.CANCEL);
        caisseRepository.save(caisse);
        notificationService.sendCancelNotificationByClient(idCaisse);
    }

    @Override
    public void SetStatutBeingdelivred(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.BEING_DELIVERED);
        caisseRepository.save(caisse);
    }

    @Override
    public void SetStatutdelivred(String idCaisse) {
        Caisse caisse = getCaisseById(idCaisse);
        caisse.setStatus(Status.DELIVERED);
        caisseRepository.save(caisse);
        User delivery = userRepository.findById(caisse.getIdDelivery())
                .orElseThrow(() -> new NoSuchElementException("user not found with ID" + caisse.getIdDelivery()));
        List<RevenueDate> revenueDates = delivery.getRevenueDates();
        if (revenueDates.size() != 0) {
            boolean dateFound = false;
            for (RevenueDate revenueDate : revenueDates) {
                if (revenueDate.getDate().equals(caisse.getDate())) {
                    int newRevenue = revenueDate.getRevenue() + 2;
                    revenueDate.setRevenue(newRevenue);
                    revenueDateRepository.save(revenueDate);
                    userRepository.save(delivery);
                    dateFound = true;
                    break;
                }
            }

            if (!dateFound) {
                RevenueDate revenueDate1 = new RevenueDate(2, caisse.getDate());
                revenueDateRepository.save(revenueDate1);
                delivery.getRevenueDates().add(revenueDate1);
                userRepository.save(delivery);
            }
        } else {
            // Create a new revenue date if the list is empty
            RevenueDate revenueDate1 = new RevenueDate(2, caisse.getDate());
            revenueDateRepository.save(revenueDate1);
            delivery.getRevenueDates().add(revenueDate1);
            userRepository.save(delivery);
        }
    }

    @Override
    public void SetSold(String idCaisse) {

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
            if (caisse.getStatus() == Status.DELIVERED) {
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
            if (caisse.getStatus() == Status.CANCEL) {
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
    public int calculateTotalRevenueForAllCaisse(String id) {
        List<Caisse> caisses = caisseRepository.findByidDelivery(id);
        int totalRevenue = 0;

        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.DELIVERED) {
                User delivery = userRepository.findById(caisse.getIdDelivery())
                        .orElseThrow(() -> new NoSuchElementException("user not found with ID" + caisse.getIdDelivery()));
                List<RevenueDate> revenueDates = delivery.getRevenueDates();
                for (RevenueDate revenueDate : revenueDates) {
                    totalRevenue += revenueDate.getRevenue();
                }
            }
        }

        return totalRevenue;
    }



    @Override
    public int getDeliveredCountForVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        int deliveredCount = 0;
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.DELIVERED) {
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
            if (caisse.getStatus() == Status.CANCEL) {
                cancelCount++;
            }
        }
        return cancelCount;
    }


    public void benefitsVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        if (!caisses.isEmpty()) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));
            List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();
            Map<LocalDate, BenifitsVendor> benifitsVendorMap = new HashMap<>();

            // Vérifier si les bénéfices existent déjà pour les dates des caisses
            Set<LocalDate> existingDates = benifitsVendors.stream()
                    .map(BenifitsVendor::getDate)
                    .collect(Collectors.toSet());

            // Supprimer les bénéfices existants pour les dates des caisses
            benifitsVendors.removeIf(benifitsVendor -> existingDates.contains(benifitsVendor.getDate()));

            for (Caisse caisse : caisses) {
                if (caisse.getStatus() == Status.DELIVERED) {
                    LocalDate caisseDate = caisse.getDate();

                    BenifitsVendor benifitsVendor = benifitsVendorMap.get(caisseDate);
                    if (benifitsVendor == null) {
                        benifitsVendor = new BenifitsVendor(0, 0, 0, caisseDate);
                        benifitsVendorMap.put(caisseDate, benifitsVendor);
                    }
                    double newChiffre = benifitsVendor.getChiffre() + caisse.getSubTotal();
                    benifitsVendor.setChiffre(newChiffre);
                    int newFrais = benifitsVendor.getFrais() + 1;
                    benifitsVendor.setFrais(newFrais);
                    int newBenefits = (int) (newChiffre - newFrais);
                    benifitsVendor.setBenefits(newBenefits);
                }
            }

            benifitsVendorRepository.saveAll(benifitsVendorMap.values());
            user.getBenifitsVendors().addAll(benifitsVendorMap.values());
            userRepository.save(user);
        }
    }

    public double calculateTotalRevenue(String id) {
        double totalRevenue = 0;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();

        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            totalRevenue += benifitsVendor.getChiffre();
        }

        return totalRevenue;
    }

    public double calculateTotalFrais(String id) {
        double totalFrais = 0;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();

        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            totalFrais += benifitsVendor.getFrais();
        }

        return totalFrais;
    }
    public double calculateTotalbenifits(String id) {
        double totalbenifits = 0;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        List<BenifitsVendor> benifitsVendors = user.getBenifitsVendors();

        for (BenifitsVendor benifitsVendor : benifitsVendors) {
            totalbenifits+= benifitsVendor.getBenefits();
        }

        return totalbenifits;
    }

    @Override
    public List<User> getListClientByVendor(String id) {
        List<Caisse> caisses = caisseRepository.findByidVendor(id);
        List<User> users = new ArrayList<>();
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.DELIVERED) {
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
            if (caisse.getStatus() == Status.DELIVERED) {
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
    public Map<String, List<String>> regrouperAvisParArticle() {
        Map<String, List<String>> avisParArticle = new HashMap<>();
        List<Caisse> caisses = caisseRepository.findAll();
        for (Caisse caisse : caisses) {
            if (caisse.getStatus() == Status.DELIVERED) {
                List<ArticleCaisse> articles = caisse.getArticles();
                for (ArticleCaisse articleCaisse : articles) {
                    String idArticle = articleCaisse.getIdArticle();
                    String avis = articleCaisse.getAvis();

                    if (avis != null) { // Exclude null values
                        List<String> avisList = avisParArticle.getOrDefault(idArticle, new ArrayList<>());
                        avisList.add(avis);
                        avisParArticle.put(idArticle, avisList);
                    }
                }
            }
        }

        return avisParArticle;
    }


}



















