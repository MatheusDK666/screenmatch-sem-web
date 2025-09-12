package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService("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");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}
