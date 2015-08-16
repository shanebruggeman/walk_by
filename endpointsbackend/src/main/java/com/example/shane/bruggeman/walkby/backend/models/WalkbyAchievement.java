package com.example.shane.bruggeman.walkby.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class WalkbyAchievement {

    @Id
    Long id;

    String description;
    Integer achievementValue;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAchievementValue() {
        return achievementValue;
    }

    public void setAchievementValue(Integer achievementValue) {
        this.achievementValue = achievementValue;
    }
}
