package com.example.readify.Manager;

import com.example.readify.Models.Member;
import java.util.ArrayList;
import java.util.HashMap;

public class MemberManager {

    private static ArrayList<Member> memberList = new ArrayList<>();
    private static HashMap<String, Member> memberMap = new HashMap<>();

    public static boolean addMember(Member member) {

        if (memberMap.containsKey(member.getEmail())) {
            return false;
        }

        memberList.add(member);
        memberMap.put(member.getEmail(), member);
        return true;
    }
}
