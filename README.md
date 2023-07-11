# formalizacao-digital-desafio
Aplicação do Banco - Formalização Digital
Desafio proposto pela equipe de formalização digital do Santander para avaliação de processo seletivo.

# Funcionalidades Principais
Cadastro de Clientes: Permite o cadastro de novos clientes no sistema, fornecendo informações como nome, CPF e dados de contato.

Simulação de Cartão de Crédito: Realiza simulações de cartões de crédito para os clientes com base em critérios como renda, histórico financeiro e pagamento de contas. Essas simulações fornecem uma classificação do tipo de cartão de crédito mais adequado para cada cliente.

Contratação de Cartão de Crédito: Permite a contratação efetiva do cartão de crédito escolhido pelo cliente após a simulação. Realiza validações adicionais e finaliza o processo de formalização.

# Arquitetura e Componentes
A aplicação Formalização Digital segue uma arquitetura baseada em microsserviços, dividida em três principais componentes:

API de Clientes: Responsável pelo cadastro e gerenciamento dos dados dos clientes. Fornece endpoints para a criação, atualização e busca de informações dos clientes.

API de Simulação: Realiza a simulação de cartões de crédito para os clientes com base em diferentes critérios. Utiliza serviços externos para calcular a pontuação do cliente e determinar a classificação do cartão.

API de Contratação: Gerencia o processo de contratação de cartões de crédito. Realiza validações adicionais, verifica a disponibilidade do cartão escolhido e registra as informações necessárias para finalizar a formalização.

# Observações
Algumas partes do código foram escritas e estabelecidas com randoms(para simular uma situação fictícia do cliente), portanto testes poderão falhar em alguns acasos, caso queiram 100%, necessário remover e colocar valores fixos.

# Conclusão
No mais, agradeço pela oportunidade de participar do processo, qualquer dúvida só entrar em contato pelo e-mail que foi enviado para vocês. Obrigado!
