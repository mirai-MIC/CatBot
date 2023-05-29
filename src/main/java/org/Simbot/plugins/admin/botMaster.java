//package org.Simbot.plugins.admin;
//
//import lombok.extern.slf4j.Slf4j;
//import love.forte.simboot.annotation.Filter;
//import love.forte.simboot.annotation.Listener;
//import love.forte.simbot.bot.Bot;
//import love.forte.simbot.event.FriendMessageEvent;
//import org.springframework.stereotype.Component;
//
///**
// * @BelongsProject: simbot
// * @BelongsPackage: org.Simbot.plugins.admin
// * @Author: MIC
// * @CreateTime: 2023-03-07  16:38
// * @Description:
// * @Version: 1.0
// */
//
//@Component
//@Slf4j
//public class botMaster {
//
//    @Listener
//    @Filter(value = "/群组")
//    public void botGroup(FriendMessageEvent event) {
//        Bot bot = event.getBot();
//        bot.getGroups().asStream().forEach(a->{
//            System.out.println(a.getName());
//            System.out.println(a.getId());
//            a.getMembers().asStream().forEach(b->{
//                System.out.println(b.getNickOrUsername());
//            });
//        });
//
//        for (Group group : bot.getGroups().collectToList()) {
//            System.out.println(group.getName());
//            System.out.println(group.getId());
//            group.getMembers().collectToList().forEach(a->{
//                System.out.println(a.getNickOrUsername());
//            });
//        }
//
//        Group group = bot.getGroup(ID.$("620428906"));
//        for (GroupMember groupMember : group.getMembers().collectToList()) {
//            System.out.println(groupMember.getNickOrUsername());
//        }
//    }
//
//}
