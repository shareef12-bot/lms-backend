package com.lms.search.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.search.model.SearchIndex;
import com.lms.search.repository.SearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CourseEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SearchRepository repository;

    public CourseEventConsumer(SearchRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
        topics = "${topics.course}",
        groupId = "search-group"
    )
    public void consume(String message) {
        try {
            System.out.println("🔍 [SEARCH] COURSE EVENT RECEIVED → " + message);

            JsonNode root = objectMapper.readTree(message);

            // Only handle course creation
            if (!"COURSE_CREATED".equals(root.get("type").asText())) {
                return;
            }

            JsonNode payload = root.get("payload");

            SearchIndex index = new SearchIndex();
            index.setCourseId(payload.get("courseId").asLong());
            index.setCourseTitle(payload.get("title").asText());
            index.setCourseDescription(
                "Learn " + payload.get("title").asText()
            );

            repository.save(index);

            System.out.println("✅ COURSE SAVED INTO SEARCH_INDEX TABLE");

        } catch (Exception e) {
            System.err.println("❌ SEARCH INDEXING FAILED");
            e.printStackTrace();
        }
    }
}
