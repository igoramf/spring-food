package com.ufcg.psoft.commerce.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.models.Cliente;
import com.ufcg.psoft.commerce.models.Entregador;
import com.ufcg.psoft.commerce.models.Estabelecimento;
import com.ufcg.psoft.commerce.models.Pedido;
import com.ufcg.psoft.commerce.models.Pizza;
import com.ufcg.psoft.commerce.models.Sabor;
import com.ufcg.psoft.commerce.models.Veiculo;
import com.ufcg.psoft.commerce.repositories.ClienteRepository;
import com.ufcg.psoft.commerce.repositories.EntregadorRepository;
import com.ufcg.psoft.commerce.repositories.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repositories.PedidoRepository;
import com.ufcg.psoft.commerce.repositories.PizzaRepository;
import com.ufcg.psoft.commerce.repositories.SaborRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de pedidos")
public class PedidoControllerTests {
    final String URI_PEDIDOS = "/v1/pedidos";

    @Autowired
    MockMvc driver;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    PizzaRepository pizzaRepository;
    @Autowired
    EntregadorRepository entregadorRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    Cliente cliente;
    Entregador entregador;
    Veiculo veiculo;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizzaM;
    Pizza pizzaG;
    Estabelecimento estabelecimento;
    Pedido pedido;
    Pedido pedido1;

    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO2;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        Set<Sabor> cardapio = new HashSet<Sabor>();

        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .cardapio(cardapio)
                .build());

        sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .valorMedia(10.0)
                .valorGrande(20.0)
                .disponivel(true)
                .estabelecimento(estabelecimento)
                .build());
        sabor2 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .valorMedia(15.0)
                .valorGrande(30.0)
                .disponivel(true)
                .estabelecimento(estabelecimento)
                .build());
        cardapio.add(sabor1);
        estabelecimento.setCardapio(cardapio);

        cliente = clienteRepository.save(Cliente.builder()
                .nomeCompleto("Anton Ego")
                .endereco("Paris")
                .codigoAcesso("123456")
                .usuario("usuario123")
                .build());

        veiculo = Veiculo.builder()
                .placa("ABC-1234")
                .cor("Azul")
                .tipo("Moto")
                .build();

        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Joãozinho")
                .veiculo(veiculo)
                .codigoAcesso("101010")
                .estabelecimento(estabelecimento)
                .build());

        pizzaM = Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build();

        pizzaG = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();

        pizzaM = pizzaRepository.save(pizzaM);
        pizzaG = pizzaRepository.save(pizzaG);

        List<Pizza> pizzas = List.of(pizzaM);
        List<Pizza> pizzas1 = List.of(pizzaM, pizzaG);

        pedido = Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .cliente(cliente)
                .estabelecimento(estabelecimento)
//                                        .entregador(entregador)
                .pizzas(pizzas)
                .build();

        pedido1 = Pedido.builder()
                .preco(12.5)
                .enderecoEntrega("Casa 237")
                .cliente(cliente)
                .estabelecimento(estabelecimento)
//                                        .entregador(entregador)
                .pizzas(pizzas1)
                .build();

        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega(pedido.getEnderecoEntrega())
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pedido.getPizzas())
                .build();


    }

    @AfterEach
    void tearDown() {
//                	pedidoRepository.deleteById(1L);
        // pizzaRepository.deleteAll();
        // pedidoRepository.deleteAll();
        // clienteRepository.deleteAll();
        // saborRepository.deleteAll();
        // estabelecimentoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class PedidoVerificacaoFluxosBasicosApiRest {

        // @Test
        // @DisplayName("Quando criamos um novo pedido com dados válidos")
        // void quandoCriamosUmNovoPedidoComDadosValidos() throws Exception {
        //     // Arrange

        //     // Act
        //     String responseJsonString = driver.perform(post(URI_PEDIDOS)
        //                     .contentType(MediaType.APPLICATION_JSON)
        //                     .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
        //                     .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
        //             .andExpect(status().isCreated())
        //             .andDo(print())// Codigo 201
        //             .andReturn().getResponse().getContentAsString();

        //     PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);

        //     // Assert
        //     assertAll(
        //             // () -> assertNotNull(resultado.getId()),
        //             () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
        //             () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
        //             () -> assertEquals(pedido.getCliente().getId(), resultado.getCliente().getId()),
        //             () -> assertEquals(pedido.getEstabelecimento().getId(), resultado.getEstabelecimento().getId()),
        //             () -> assertEquals(pedido.getPreco(), resultado.getPreco()));
        // }

        // @Test
        // @DisplayName("Quando alteramos um novo pedido com dados válidos")
        // void quandoAlteramosPedidoValido() throws Exception {
        //     // Arrange
        //     pedidoRepository.save(pedido);
        //     Sabor sabor3 = Sabor.builder()
        //             .nome("Calabresa")
        //             .tipo("Salgado")
        //             .valorGrande(20.0)
        //             .valorMedia(15)
        //             .disponivel(true)
        //             .estabelecimento(estabelecimento)
        //             .build();

        //     Sabor sabor4 = Sabor.builder()
        //             .nome("Calabresa")
        //             .tipo("Salgado")
        //             .valorGrande(20.0)
        //             .valorMedia(15)
        //             .disponivel(true)
        //             .estabelecimento(estabelecimento)
        //             .build();

        //     saborRepository.save(sabor3);
        //     saborRepository.save(sabor4);


        //     Pizza pizzaG2 = Pizza.builder()
        //             .sabor1(sabor3)
        //             .sabor2(sabor4)
        //             .tamanho("grande")
        //             .build();

        //     List<Pizza> pizzas2 = List.of(pizzaG2);

        //     Long pedidoId = pedido.getId();

        //     PedidoPutRequestDTO pedidoPutRequestDTO = PedidoPutRequestDTO.builder()
        //             .enderecoEntrega("Rua Manoel Paulino")
        //             .pizzas(pizzas2)
        //             .build();


        //     // Act
        //     String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedidoId)
        //                     .contentType(MediaType.APPLICATION_JSON)
        //                     //.param("pedidoId", pedido.getId().toString())
        //                     .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
        //                     .content(objectMapper.writeValueAsString(pedidoPutRequestDTO)))
        //             .andExpect(status().isOk())
        //             .andDo(print())
        //             .andReturn().getResponse().getContentAsString();

        //     PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString,
        //             PedidoResponseDTO.class);

        //     // Assert
        //     assertAll(
        //             () -> assertEquals(pedidoId, resultado.getId().longValue()),
        //             () -> assertEquals(pedidoPutRequestDTO.getEnderecoEntrega(),
        //                     resultado.getEnderecoEntrega()),
        //             //() -> assertEquals("j", "k"),
        //             () -> assertEquals(pedidoPutRequestDTO.getPizzas().get(0).getSabor1(),
        //                     resultado.getPizzas().get(0).getSabor1()),
        //             () -> assertEquals(pedido.getCliente(), resultado.getCliente()),
        //             () -> assertEquals(pedido.getEstabelecimento(),
        //                     resultado.getEstabelecimento()),
        //             () -> assertEquals(pedido.getPreco(), resultado.getPreco())
        //     );
        // }
//
//
//
//        @Test
//        @DisplayName("Quando alteramos um pedido inexistente")
//        void quandoAlteramosPedidoInexistente() throws Exception {
//            // Arrange
//            // nenhuma necessidade além do setup()
//
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("pedidoId", "999999")
//                            .param("codigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);
//
//            // Assert
//            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
//        }
//
//        @Test
//        @DisplayName("Quando alteramos um pedido passando codigo de acesso invalido")
//        void quandoAlteramosPedidoPassandoCodigoAcessoInvalido() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("pedidoId", pedido.getId().toString())
//                            .param("codigoAcesso", "999999")
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
//        }
//
        // @Test
        // @DisplayName("Quando um cliente busca por todos seus pedidos salvos")
        // void quandoClienteBuscaTodosPedidos() throws Exception {
        //     // Arrange
        //     pedidoRepository.save(pedido);
        //     pedidoRepository.save(pedido1);

        //     // Act
        //     String responseJsonString = driver.perform(get(URI_PEDIDOS)
        //                     .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
        //                     .contentType(MediaType.APPLICATION_JSON))
        //                     //.content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
        //             .andExpect(status().isOk())
        //             .andDo(print())
        //             .andReturn().getResponse().getContentAsString();

        //     List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

        //     // Assert
        //     assertEquals(2, resultado.size());
        // }

        // @Test
        // @DisplayName("Quando um cliente busca por um pedido seu salvo pelo id primeiro")
        // void quandoClienteBuscaPedidoPorId() throws Exception {
        //     // Arrange
        //     pedidoRepository.save(pedido);

        //     // Act
        //     String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
        //                     pedido.getId() + "/" + cliente.getId())
        //                     .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
        //                     .contentType(MediaType.APPLICATION_JSON))
        //                     //.content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
        //             .andExpect(status().isOk())
        //             .andDo(print())
        //             .andReturn().getResponse().getContentAsString();

        //     PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {});

        //     //Pedido resultado = listaResultados.get(0);

        //     // Assert
        //     assertAll(
        //             () -> assertNotNull(resultado.getId()),
        //             () -> assertEquals(pedido.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
        //             () -> assertEquals(pedido.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
        //             () -> assertEquals(pedido.getCliente(), resultado.getCliente()),
        //             () -> assertEquals(pedido.getEstabelecimento(), resultado.getEstabelecimento()),
        //             () -> assertEquals(pedido.getPreco(), resultado.getPreco())
        //     );
        // }
//
//        @Test
//        @DisplayName("Quando um cliente busca por um pedido seu salvo por id inexistente")
//        void quandoClienteBuscaPedidoInexistente() throws Exception {
//            // Arrange
//            // nenhuma necessidade além do setup()
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "999999" +
//                            "/" + cliente.getId())
//                            .param("clienteId", cliente.getId().toString())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);
//
//            // Assert
//            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
//        }
//
        // @Test
        // @DisplayName("Quando um cliente busca por um pedido feito por outro cliente")
        // void quandoClienteBuscaPedidoDeOutroCliente() throws Exception {
        //     // Arrange
        //     pedidoRepository.save(pedido);
        //     Cliente cliente1 = clienteRepository.save(Cliente.builder()
        //             .nomeCompleto("Catarina")
        //             .endereco("Casinha")
        //             .codigoAcesso("121212")
        //             .usuario("catarina123")
        //             .build());

        //     // Act
        //     String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
        //                     pedido.getId() + "/" + cliente1.getId())
        //                     .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
        //                     .contentType(MediaType.APPLICATION_JSON))
        //                     //.content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
        //             .andExpect(status().isBadRequest())
        //             .andDo(print())
        //             .andReturn().getResponse().getContentAsString();

        //     CustomErrorType resultado = objectMapper.readValue(responseJsonString,
        //             CustomErrorType.class);

        //     // Assert
        //     assertEquals("Clientes distintos!", resultado.getMessage());
        // }
//
//
//        @Test
//        @DisplayName("Quando um estabelecimento busca todos os pedidos feitos nele")
//        void quandoEstabelecimentoBuscaTodosPedidos() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//            pedidoRepository.save(pedido1);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            estabelecimento.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            List<Pedido> resultado = objectMapper.readValue(responseJsonString, new
//                    TypeReference<>() {
//                    });
//
//            // Assert
//            assertEquals(2, resultado.size());
//        }
//
//        @Test
//        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id primeiro")
//        void quandoEstabelecimentoBuscaPedidoPorId() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            pedido.getId() + "/" + estabelecimento.getId() + "/" +
//                            estabelecimento.getCodigoAcesso())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            List<Pedido> listaResultados = objectMapper.readValue(responseJsonString, new
//                    TypeReference<>() {
//                    });
//
//            Pedido resultado = listaResultados.get(0);
//
//            // Assert
//            assertAll(
//                    () -> assertNotNull(resultado.getId()),
//                    () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
//                    () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
//                    () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
//                    () -> assertEquals(pedido.getEstabelecimentoId(),
//                            resultado.getEstabelecimentoId()),
//                    () -> assertEquals(pedido.getPreco(), resultado.getPreco())
//            );
//        }
//
//        @Test
//        @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id inexistente")
//        void quandoEstabelecimentoBuscaPedidoInexistente() throws Exception {
//            // Arrange
//            // nenhuma necessidade além do setup()
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            estabelecimento.getCodigoAcesso() + "/" + estabelecimento.getId() + "/" +
//                            "999999")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
//        }
//
//        @Test
//        @DisplayName("Quando um estabelecimento busca por um pedido feito em outro estabelecimento")
//        void quandoEstabelecimentoBuscaPedidoDeOutroEstabelecimento() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//            Estabelecimento estabelecimento1 =
//                    estabelecimentoRepository.save(Estabelecimento.builder()
//                            .codigoAcesso("121212")
//                            .build());
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            pedido.getId() + "/" + estabelecimento1.getId() + "/" +
//                            estabelecimento1.getCodigoAcesso())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
//        }
//
        @Test
        @DisplayName("Quando um cliente excluí um pedido feito por ele salvo")
        void quandoClienteExcluiPedidoSalvo() throws Exception {
                // Arrange

                pedidoRepository.save(pedido);

                // Act
                String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
                                pedido.getId() + "/" + cliente.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("codigoAcesso", cliente.getCodigoAcesso()))
                        .andExpect(status().isNoContent())
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();

                // Assert
                assertTrue(responseJsonString.isBlank());
        }
//
//        @Test
//        @DisplayName("Quando um cliente excluí um pedido inexistente")
//        void quandoClienteExcluiPedidoInexistente() throws Exception {
//            // Arrange
//            // nenhuma necessidade além do setup()
//
//            // Act
//            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
//                            "999999" + "/" + cliente.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
//        }
//
       @Test
       @DisplayName("Quando um cliente excluí todos seus pedidos feitos por ele salvos")
       void quandoClienteExcluiTodosPedidosSalvos() throws Exception {
           // Arrange
           pedidoRepository.save(pedido);
           pedidoRepository.save(Pedido.builder()
                   .preco(10.0)
                   .enderecoEntrega("Casa 237")
                   .cliente(cliente)
                   .estabelecimento(estabelecimento)
                   .pizzas(List.of(pizzaM, pizzaG))
                   .build());

           // Act
           String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + cliente.getId())
                           .param("codigoAcesso", cliente.getCodigoAcesso())
                           .contentType(MediaType.APPLICATION_JSON))
                   .andExpect(status().isNoContent())
                   .andDo(print())
                   .andReturn().getResponse().getContentAsString();

           // Assert
           assertTrue(responseJsonString.isBlank());
       }

       @Test
       @DisplayName("Quando um estabelencimento excluí um pedido feito nele salvo")
       void quandoEstabelecimentoExcluiPedidoSalvo() throws Exception {
           // Arrange
           pedidoRepository.save(pedido);

           // Act
                String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
                        pedido.getId() + "/" + estabelecimento.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("codigoAcesso", cliente.getCodigoAcesso()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        //    String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
        //                    pedido.getId() + "/" + estabelecimento.getId())
        //                    .contentType(MediaType.APPLICATION_JSON)
        //                    .param("clienteCodigoAcesso", estabelecimento.getCodigoAcesso()))
        //            .andExpect(status().isNoContent())
        //            .andDo(print())
        //            .andReturn().getResponse().getContentAsString();

           // Assert
           assertTrue(responseJsonString.isBlank());
       }
// 
//        @Test
//        @DisplayName("Quando um estabelencimento excluí um pedido inexistente")
//        void quandoEstabelecimentoExcluiPedidoInexistente() throws Exception {
//            // Arrange
//            // nenhuma necessidade além do setup()
//
//            // Act
//            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
//                            "999999" + "/" + estabelecimento.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
//        }
//
//        @Test
//        @DisplayName("Quando um estabelencimento excluí um pedido feito em outro estabelecimento")
//        void quandoEstabelecimentoExcluiPedidoDeOutroEstabelecimento() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//            Estabelecimento estabelecimento1 =
//                    estabelecimentoRepository.save(Estabelecimento.builder()
//                            .codigoAcesso("121212")
//                            .build());
//
//            // Act
//            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
//                            pedido.getId() + "/" + estabelecimento1.getId() + "/" +
//                            estabelecimento1.getCodigoAcesso())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcesso", estabelecimento1.getCodigoAcesso()))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("Codigo de acesso invalido!", resultado.getMessage());
//        }
//
        @Test
        @DisplayName("Quando um estabelencimento excluí todos os pedidos feitos nele salvos")
        void quandoEstabelecimentoExcluiTodosPedidosSalvos() throws Exception {
        // Arrange
        pedidoRepository.save(pedido);
        pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .cliente(cliente)
                .estabelecimento(estabelecimento)
                .pizzas(List.of(pizzaM, pizzaG))
                .build());

        // Act
        String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
                        estabelecimento.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        // Assert
        assertTrue(responseJsonString.isBlank());
       }
//
//        @Test
//        @DisplayName("Quando um cliente cancela um pedido")
//        void quandoClienteCancelaPedido() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
//                            pedido.getId() + "/cancelar-pedido")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isNoContent())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            // Assert
//            assertTrue(responseJsonString.isBlank());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento")
//        void quandoClienteBuscaPedidoFeitoEmEstabelecimento() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" +
//                            estabelecimento.getId() + "/" + pedido.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//
//
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            List<PedidoResponseDTO> resultado =
//                    objectMapper.readValue(responseJsonString, new TypeReference<>() {
//                    });
//
//            // Assert
//            assertEquals(1, resultado.size());
//            assertEquals(pedido.getId(), resultado.get(0).getId());
//            assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
//            assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento inexistente")
//        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoInexistente() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + "999999" +
//                            "/" + pedido.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("O estabelecimento consultado nao existe!",
//                    resultado.getMessage());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com pedido inexistente")
//        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComPedidoInexistente() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" +
//                            estabelecimento.getId() + "/" + "999999")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("O pedido consultado nao existe!", resultado.getMessage());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com cliente inexistente")
//        void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComClienteInexistente() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" +
//                            "pedido-cliente-estabelecimento" + "/" + "999999" + "/" +
//                            estabelecimento.getId() + "/" + pedido.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isBadRequest())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            CustomErrorType resultado = objectMapper.readValue(responseJsonString,
//                    CustomErrorType.class);
//
//            // Assert
//            assertEquals("O cliente consultado nao existe!", resultado.getMessage());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com pedidoId null")
//        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidoIdNull() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS +
//                            "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" +
//                            estabelecimento.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            List<PedidoResponseDTO> resultado =
//                    objectMapper.readValue(responseJsonString, new TypeReference<>() {
//                    });
//
//            // Assert
//            assertEquals(1, resultado.size());
//            assertEquals(pedido.getId(), resultado.get(0).getId());
//            assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
//            assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com status")
//        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComStatus() throws Exception {
//            // Arrange
//            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
//                    .preco(30.0)
//                    .enderecoEntrega("Casa 237")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .pizzas(List.of(pizzaM))
//                    .statusEntrega("Pedido em preparo")
//                    .build());
//
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS +
//                            "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" +
//                            estabelecimento.getId() + "/" + pedido3.getStatusEntrega())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            List<PedidoResponseDTO> resultado =
//                    objectMapper.readValue(responseJsonString, new TypeReference<>() {
//                    });
//
//            // Assert
//            assertEquals(1, resultado.size());
//            assertEquals(pedido3.getId(), resultado.get(0).getId());
//            assertEquals(pedido3.getClienteId(), resultado.get(0).getClienteId());
//            assertEquals(pedido3.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
//        }
//
//        @Test
//        @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento filtrados por entrega")
//        void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidosFiltradosPorEntrega() throws Exception {
//            // Arrange
//            Pedido pedido3 = pedidoRepository.save(Pedido.builder()
//                    .preco(30.0)
//                    .enderecoEntrega("Casa 237")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .pizzas(List.of(pizzaM))
//                    .statusEntrega("Pedido entregue")
//                    .build());
//            Pedido pedido4 = pedidoRepository.save(Pedido.builder()
//                    .preco(30.0)
//                    .enderecoEntrega("Casa 237")
//                    .clienteId(cliente.getId())
//                    .estabelecimentoId(estabelecimento.getId())
//                    .pizzas(List.of(pizzaM))
//                    .statusEntrega("Pedido em preparo")
//                    .build());
//
//            // Act
//            String responseJsonString = driver.perform(get(URI_PEDIDOS +
//                            "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" +
//                            estabelecimento.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            List<PedidoResponseDTO> resultado =
//                    objectMapper.readValue(responseJsonString, new TypeReference<>() {
//                    });
//
//            // Assert
//            assertEquals(2, resultado.size());
//            assertEquals(pedido4.getId(), resultado.get(0).getId());
//            assertEquals(pedido4.getClienteId(), resultado.get(0).getClienteId());
//            assertEquals(pedido4.getEstabelecimentoId(),
//                    resultado.get(0).getEstabelecimentoId());
//            assertEquals(pedido3.getId(), resultado.get(1).getId());
//            assertEquals(pedido3.getClienteId(), resultado.get(1).getClienteId());
//            assertEquals(pedido3.getEstabelecimentoId(),
//                    resultado.get(1).getEstabelecimentoId());
//
//        }
    }
//
//    @Nested
//    @DisplayName("Alteração de estado de pedido")
//    public class AlteracaoEstadoPedidoTest {
//        Pedido pedido1;
//
//        @BeforeEach
//        void setUp() {
//            pedido1 = pedidoRepository.save(Pedido.builder()
//                    .estabelecimentoId(estabelecimento.getId())
//                    .clienteId(cliente.getId())
//                    .enderecoEntrega("Rua 1")
//                    .pizzas(List.of(pizzaG))
//                    .preco(10.0)
//                    .build()
//            );
//        }
//
//        @Test
//
//        @DisplayName("Quando o estabelecimento associa um pedido a um entregador")
//        void quandoEstabelecimentoAssociaPedidoEntregador() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//            pedido.setStatusEntrega("Pedido pronto");
//            entregador.setStatusAprovacao(true);
//            List<Entregador> entregadores = new LinkedList<>();
//            entregadores.add(entregador);
//            estabelecimento.setEntregadoresDisponiveis(entregadores);
//            entregador.setDisponibilidade(true);
//
//
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" +
//                            pedido.getId() + "/" + "/associar-pedido-entregador")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("estabelecimentoId", estabelecimento.getId().toString())
//                            .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString,
//                    PedidoResponseDTO.class);
//
//            // Assert
//            assertEquals(resultado.getStatusEntrega(), "Pedido em rota");
//            assertEquals(entregador.getId(), resultado.getEntregadorId());
//        }
//
//        @Test
//
//        @DisplayName("Quando o cliente confirma a entrega de um pedido")
//        void quandoClienteConfirmaEntregaPedido() throws Exception {
//            // Arrange
//            pedidoRepository.save(pedido);
//            pedido.setStatusEntrega("Pedido em rota");
//
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" +
//                            pedido.getId() + "/" + cliente.getId() + "/cliente-confirmar-entrega")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn().getResponse().getContentAsString();
//
//            PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString,
//                    PedidoResponseDTO.class);
//
//            // Assert
//            assertEquals(resultado.getStatusEntrega(), "Pedido entregue");
//        }
//    }
//
//    @Nested
//    @DisplayName("Conjunto de casos de teste da confirmação de pagamento de um pedido")
//    public class PedidoConfirmarPagamentoTests {
//
//        Pedido pedido1;
//
//        @BeforeEach
//        void setUp() {
//            pedido1 = pedidoRepository.save(Pedido.builder()
//                    .estabelecimentoId(estabelecimento.getId())
//                    .clienteId(cliente.getId())
//                    .enderecoEntrega("Rua 1")
//                    .pizzas(List.of(pizzaG))
//                    .preco(10.0)
//                    .build()
//            );
//        }
//
//        @Test
//        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
//        void confirmaPagamentoCartaoCredito() throws Exception {
//            // Arrange
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" +
//                            cliente.getId() + "/confirmar-pagamento")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcessoCliente", cliente.getCodigoAcesso())
//                            .param("pedidoId", pedido1.getId().toString())
//                            .param("metodoPagamento", "Cartão de crédito")
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk()) // Codigo 200
//                    .andReturn().getResponse().getContentAsString();
//            // Assert
//            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
//            assertAll(
//                    () -> assertTrue(resultado.getStatusPagamento()),
//                    () -> assertEquals(10, resultado.getPreco())
//            );
//        }
//
//        @Test
//        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
//        void confirmaPagamentoCartaoDebito() throws Exception {
//            // Arrange
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" +
//                            cliente.getId() + "/confirmar-pagamento")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcessoCliente", cliente.getCodigoAcesso())
//                            .param("pedidoId", pedido1.getId().toString())
//                            .param("metodoPagamento", "Cartão de débito")
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk()) // Codigo 200
//                    .andReturn().getResponse().getContentAsString();
//            // Assert
//            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
//            assertAll(
//                    () -> assertTrue(resultado.getStatusPagamento()),
//                    () -> assertEquals(9.75, resultado.getPreco())
//            );
//        }
//
//        @Test
//        @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
//        void confirmaPagamentoPIX() throws Exception {
//            // Arrange
//            // Act
//            String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" +
//                            cliente.getId() + "/confirmar-pagamento")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .param("codigoAcessoCliente", cliente.getCodigoAcesso())
//                            .param("pedidoId", pedido1.getId().toString())
//                            .param("metodoPagamento", "PIX")
//                            .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
//                    .andExpect(status().isOk()) // Codigo 200
//                    .andReturn().getResponse().getContentAsString();
//            // Assert
//            Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);
//            assertAll(
//                    () -> assertTrue(resultado.getStatusPagamento()),
//                    () -> assertEquals(9.5, resultado.getPreco())
//            );
//        }
//    }
}

