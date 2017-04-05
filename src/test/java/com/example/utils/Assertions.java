package com.example.utils;

import com.example.controller.vm.MessageVM;
import com.example.service.gitter.dto.MessageResponse;
import org.junit.Assert;

import java.util.Iterator;
import java.util.List;

public final class Assertions {
    private Assertions() {
    }

    public static void assertMessages(List<MessageVM> messages) {
        for (int i = 0; i < messages.size(); i++) {
            MessageVM messageVM = messages.get(i);
            String expected = String.valueOf(i);

            Assert.assertEquals(expected, messageVM.getId());
            Assert.assertEquals(expected, messageVM.getText());
            Assert.assertEquals(expected, messageVM.getHtml());
            Assert.assertEquals(expected, messageVM.getUserAvatarUrl());
            Assert.assertEquals(expected, messageVM.getUsername());
        }
    }

    public static void assertMessages(Iterable<MessageResponse> messages) {
        Iterator<MessageResponse> iterator = messages.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            MessageResponse message = iterator.next();

            Assert.assertEquals(ChatResponseFactory.message(String.valueOf(i)), message);
        }
    }
}