package com.example.pikachoong.responseimposs;

public class Properties {
    private int totalDistance;
    private int totalTime;
    private int totalFare;
    private int taxiFare;
    private int index;
    private int pointIndex;
    private String name;
    private String description;
    private String nextRoadName;
    private int turnType;
    private String pointType;

    public int getTotalDistance(){return totalDistance;}
    public void setTotalDistance(int totalDistance){
        this.totalDistance = totalDistance;
    }

    public int getTotalTime(){return totalTime;}
    public void setTotalTime(int totalTime){
        this.totalTime = totalTime;
    }

    public int getTotalFare(){return totalFare;}
    public void setTotalFare(int totalFare){
        this.totalFare = totalFare;
    }

    public int getTaxiFare(){return taxiFare;}
    public void setTaxiFare(int taxiFare){
        this.taxiFare = taxiFare;
    }

    public int getIndex(){return index;}
    public void setIndex(int index){
        this.index = index;
    }

    public int getPointIndex(){return pointIndex;}
    public void setPointIndex(int pointIndex){
        this.pointIndex = pointIndex;
    }

    public String getName(){return name;}
    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){return description;}
    public void setDescription(String description){
        this.description = description;
    }

    public String getNextRoadName(){return nextRoadName;}
    public void setNextRoadName(String nextRoadName){
        this.nextRoadName = nextRoadName;
    }

    public int getTurnType(){return turnType;}
    public void setTurnType(int turnType){
        this.turnType = turnType;
    }

    public String getPointType(){return pointType;}
    public void setPointType(String pointType){
        this.pointType = pointType;
    }

}
