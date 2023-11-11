package com.guilherme.FipeCars.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guilherme.FipeCars.models.*;
import com.guilherme.FipeCars.service.Api;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    Api api = new Api();
    ObjectMapper objectMapper = new ObjectMapper();

    public void menu() {
        String baseUrl = "https://parallelum.com.br/fipe/api/v1/";
        String param;
        String json;
        String menu = """
                Qual voce deseja buscar?
                Carro
                Motos
                Caminhões
                 """;
        // menu para selecionar o tipo de veiculo
        System.out.println(menu);
        String opcao = scanner.nextLine().toLowerCase();
        // tratamento da escolha
        if (opcao.contains("carr")){
            param = "carros/marcas/";
        } else if (opcao.contains("mot")) {
            param = "motos/marcas/";
        } else {
            param = "caminhoes/marcas/";
        }
        // fazendo o link para a api
        String url = baseUrl + param;
        // requisitando a api
        json = api.getData(url);
        System.out.println(json);
        List<Marca> marcas;
        // tratando a resposta
        try {
            marcas = objectMapper.readValue(json, new TypeReference<List<Marca>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro in Marcas: " + e.getMessage());
        }
        // imprimindo cada uma
        marcas.stream()
                        .forEach(e -> System.out.println("Codigo: " + e.codigo() + " Marca: " + e.nome()));
        System.out.println("Insira a opção que deseja:");
        // escolha da marca do veiculo
        opcao = scanner.nextLine();
        url +=  opcao + "/modelos/";
        json = api.getData(url);
        // tratando a resposta
        ModeloAno modeloAnos;
        try {
            modeloAnos = objectMapper.readValue(json, ModeloAno.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro in ModeloAno" + e.getMessage());
        }
        // imprimindo cada uma
        modeloAnos.modelos().stream()
                .forEach(m -> System.out.println(m.codigo() + " - " + m.nome()));
        System.out.println("Escolha uma opção:");
        // escolha do veiculo
        opcao = scanner.nextLine();
        url += opcao + "/anos/";
        json = api.getData(url);
        List<Ano> anos;
        // pegando todos os anos do veiculo escolhido
        try {
            anos = objectMapper.readValue(json, new TypeReference<List<Ano>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro in modelos: " + e.getMessage());
        }
        // pegando as informaçoes de cada ano do modelo escolhido
        List<Veiculo> veiculos = new ArrayList<>();
        String finalUrl = url;
        anos.stream()
                .forEach( a -> {
                    try {
                        veiculos.add(objectMapper.readValue(api.getData(finalUrl + a.codigo()), Veiculo.class));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        // imprimindo a lista com todos as informacoes de cada ano do modelo escolhido
        veiculos.stream()
                .forEach(v -> System.out.println("""
                        Codigo Fipe: %s
                        Marca: %s
                        Modelo: %s
                        Ano: %s
                        Combustivel: %s
                        Valor: %s
                        """.formatted(v.codigoFipe(), v.marca(), v.modelo(), v.ano(), v.combustivel(), v.valor())));
     }
}
