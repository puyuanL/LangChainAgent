package org.lpy.langchainagent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lpy.langchainagent.assistant.MedicalAgent;
import org.lpy.langchainagent.bean.ChatForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MedicalAgent")
@RestController
@RequestMapping("/medicalAgent")
public class MedicalAgentController {

    @Autowired
    private MedicalAgent medicalAgent;

    @Operation(summary = "chat")
    @PostMapping("/chat")
    public String chat(@RequestBody ChatForm chatForm) {
        return medicalAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }
}
