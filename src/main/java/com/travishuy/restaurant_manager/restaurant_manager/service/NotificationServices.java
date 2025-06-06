package com.travishuy.restaurant_manager.restaurant_manager.service;

//@Service
//public class NotificationService {
//
//    @Autowired
//    private AdminNotificationRepository adminNotificationRepository;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    private final ObjectMapper objectMapper = new ObjectMapper()
//            .registerModule(new JavaTimeModule())
//            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//
//    // Method to create and send a notification
//    public AdminNotification createNotification(String title, String message,
//                                                NotificationType type, String relatedId) {
//        AdminNotification notification = new AdminNotification();
//        notification.setTitle(title);
//        notification.setMessage(message);
//        notification.setType(type);
//        notification.setRelatedId(relatedId);
//        notification.setRead(false);
//        notification.setTimestamp(LocalDateTime.now());
//
//        // Save to database
//        AdminNotification savedNotification = adminNotificationRepository.save(notification);
//
//        try {
//            // Send to Kafka
//            String notificationJson = objectMapper.writeValueAsString(savedNotification);
//            kafkaTemplate.send("admin-notifications", notificationJson);
//
//            // Also send directly to WebSocket for immediate delivery
//            messagingTemplate.convertAndSend("/topic/admin-notifications", savedNotification);
//        } catch (JsonProcessingException e) {
//            // Simple error handling
//            System.err.println("Error sending notification: " + e.getMessage());
//        }
//
//        return savedNotification;
//    }
//
//    // Mark notification as read
//    public AdminNotification markAsRead(String notificationId) {
//        AdminNotification notification = adminNotificationRepository.findById(notificationId)
//                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
//
//        notification.setRead(true);
//        return adminNotificationRepository.save(notification);
//    }
//
//    // Get all unread notifications
//    public List<AdminNotification> getUnreadNotifications() {
//        return adminNotificationRepository.findByReadFalseOrderByTimestampDesc();
//    }
//
//    public List<AdminNotification> getAllNotifications(){
//        return adminNotificationRepository.findAll();
//    }
//}
