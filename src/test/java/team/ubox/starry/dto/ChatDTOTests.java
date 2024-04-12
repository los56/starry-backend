package team.ubox.starry.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import team.ubox.starry.service.dto.chat.ChatDTO;

import java.io.*;
import java.util.Arrays;

public class ChatDTOTests {
    
    @Test
    void SenderDTOSerializableTest() throws IOException, ClassNotFoundException {
        ChatDTO.SenderDTO senderDTO = new ChatDTO.SenderDTO("Test", "Tester");

        byte[] serialized;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(baos);
        outputStream.writeObject(senderDTO);
        serialized = baos.toByteArray();

        System.out.println("senderDTO = " + Arrays.toString(serialized));

        outputStream.close();
        baos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ObjectInputStream inputStream = new ObjectInputStream(bais);
        ChatDTO.SenderDTO deserialized = (ChatDTO.SenderDTO) inputStream.readObject();

        Assertions.assertNotNull(deserialized);
        Assertions.assertEquals(senderDTO.getId(), deserialized.getId());
        Assertions.assertEquals(senderDTO.getNickname(), deserialized.getNickname());
    }
}
