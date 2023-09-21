package org.example;

public class Main {
    public static void main(String[] args) {
        FollowDAO dao = new FollowDAO();

        dao.followUser("@Joe", "Joe", "@Bob", "Bob");
        dao.followUser("@Joe", "Joe", "@Ash", "Ash");
        dao.followUser("@Joe", "Joe", "@Carl", "Carl");
        dao.followUser("@Joe", "Joe", "@Daryl", "Daryl");
        dao.followUser("@Joe", "Joe", "@Robert", "Robert");
        dao.followUser("@Joe", "Joe", "@Eric", "Eric");
        dao.followUser("@Joe", "Joe", "@Frank", "Frank");
        dao.followUser("@Joe", "Joe", "@George", "George");
        dao.followUser("@Joe", "Joe", "@Hank", "Hank");
        dao.followUser("@Joe", "Joe", "@Josh", "Josh");
        dao.followUser("@Joe", "Joe", "@Kyle", "Kyle");
        dao.followUser("@Joe", "Joe", "@Lyle", "Lyle");
        dao.followUser("@Joe", "Joe", "@Matt", "Matt");
        dao.followUser("@Joe", "Joe", "@Nick", "Nick");
        dao.followUser("@Joe", "Joe", "@Otto", "Otto");
        dao.followUser("@Joe", "Joe", "@Paul", "Paul");
        dao.followUser("@Joe", "Joe", "@Sal", "Sal");
        dao.followUser("@Joe", "Joe", "@Thomas", "Thomas");
        dao.followUser("@Joe", "Joe", "@Ulysses", "Ulysses");
        dao.followUser("@Joe", "Joe", "@Vance", "Vance");
        dao.followUser("@Joe", "Joe", "@Wade", "Wade");
        dao.followUser("@Joe", "Joe", "@Xavier", "Xavier");
        dao.followUser("@Joe", "Joe", "@Ymir", "Ymir");
        dao.followUser("@Joe", "Joe", "@Zach", "Zach");
        dao.followUser("@Joe", "Joe", "@Jake", "Jake");


        dao.followUser("@Bob", "Bob", "@Joe", "Joe");
        dao.followUser("@Ash", "Ash", "@Joe", "Joe");
        dao.followUser("@Carl", "Carl","@Joe", "Joe");
        dao.followUser("@Daryl", "Daryl", "@Joe", "Joe");
        dao.followUser("@Robert", "Robert", "@Joe", "Joe");
        dao.followUser("@Eric", "Eric", "@Joe", "Joe");
        dao.followUser("@Frank", "Frank", "@Joe", "Joe");
        dao.followUser("@George", "George", "@Joe", "Joe");
        dao.followUser("@Hank", "Hank", "@Joe", "Joe");
        dao.followUser("@Josh", "Josh", "@Joe", "Joe");
        dao.followUser("@Kyle", "Kyle", "@Joe", "Joe");
        dao.followUser("@Lyle", "Lyle", "@Joe", "Joe");
        dao.followUser("@Matt", "Matt", "@Joe", "Joe");
        dao.followUser("@Nick", "Nick", "@Joe", "Joe");
        dao.followUser("@Otto", "Otto", "@Joe", "Joe");
        dao.followUser("@Paul", "Paul", "@Joe", "Joe");
        dao.followUser("@Sal", "Sal", "@Joe", "Joe");
        dao.followUser("@Thomas", "Thomas", "@Joe", "Joe");
        dao.followUser("@Ulysses", "Ulysses", "@Joe", "Joe");
        dao.followUser("@Vance", "Vance", "@Joe", "Joe");
        dao.followUser("@Wade", "Wade", "@Joe", "Joe");
        dao.followUser("@Xavier", "Xavier","@Joe", "Joe");
        dao.followUser("@Ymir", "Ymir", "@Joe", "Joe");
        dao.followUser("@Zach", "Zach", "@Joe", "Joe");
        dao.followUser("@Jake", "Jake", "@Joe", "Joe");

        System.out.println(dao.follows("@Joe", "@Jake").toString());

        dao.updateFollowing("@Joe", "@Jake", "Joseph", "Jacob");
        System.out.println(dao.follows("@Joe", "@Jake").toString());

        dao.unfollow("@Joe", "@Jake");
        if(dao.follows("@Joe", "@Jake") == null){
            System.out.println("This user does not follow this user");
        }
        else {
            System.out.println(dao.follows("@Joe", "@Jake").toString());
        }
        System.out.println("Followees");
        DataPage<FollowRelation> pageOfFollowees = dao.getPageOfFollowees("@Joe", 15, "");
        FollowRelation lastFollowee = new FollowRelation();
        for(FollowRelation followRelation : pageOfFollowees.getValues()){
            System.out.println(followRelation.toString());
            lastFollowee = followRelation;
        }
        if(pageOfFollowees.isHasMorePages()){
            System.out.println("Next Page");
            pageOfFollowees = dao.getPageOfFollowees("@Joe", 15, lastFollowee.getFollowee_handle());
            for(FollowRelation followRelation : pageOfFollowees.getValues()){
                System.out.println(followRelation.toString());
            }
        }

        System.out.println("\nFollowers");
        DataPage<FollowRelation> pageofFollowers = dao.getPageOfFollowers("@Joe", 15, "");
        FollowRelation lastFollower = new FollowRelation();
        for(FollowRelation followRelation : pageofFollowers.getValues()){
            System.out.println(followRelation.toString());
            lastFollower = followRelation;
        }
        if(pageofFollowers.isHasMorePages()){
            System.out.println("Next Page");
            pageofFollowers = dao.getPageOfFollowers("@Joe", 15, lastFollower.getFollower_handle());
            for(FollowRelation followRelation : pageofFollowers.getValues()){
                System.out.println(followRelation.toString());
            }
        }
    }
}