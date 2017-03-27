package com.example.harness;

import com.example.controller.vm.MessageVM;
import com.example.service.gitter.dto.MessageResponse;
import org.junit.Assert;

import java.util.Collection;
import java.util.Iterator;

public final class Assertions {
    private Assertions() {
    }

    public static void assertMessages(Collection<MessageVM> messages) {
        Iterator<MessageVM> iterator = messages.iterator();

        for (int i = 0; iterator.hasNext(); i++) {
            MessageVM messageVM = iterator.next();
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