package com.example.quit;

import java.util.List;

public class Health_Category {
    private String name;
    private List<String> facts;
    private float currentProgress;
    private float totalProgress;
    private boolean expanded;

    public Health_Category(String name, List<String> facts, float currentProgress, float totalProgress){
        this.name = name;
        this.facts = facts;
        this.currentProgress = currentProgress;
        this.totalProgress = totalProgress;
        this.expanded = false;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentProgress(){ return currentProgress;}
    public void setCurrentProgress(float currentProgress){this.currentProgress = currentProgress;}

    public float getTotalProgress(){ return totalProgress;}
    public void setTotalProgress(float totalProgress){ this.totalProgress = totalProgress;}

    public String getFacts() {
        String concatFacts = "";

        for(String fact : facts) {
            concatFacts += fact + "\n\n";
        }

        return concatFacts.substring(0, concatFacts.length() - 2);
    }

    public void setFacts(List<String> facts) {
        this.facts = facts;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

}
