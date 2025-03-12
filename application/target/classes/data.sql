-- Inserções na tabela endereco
INSERT INTO endereco (cep, rua, numero, bairro, complemento, cidade, uf)
VALUES
('12345-678', 'Rua das Flores', '123', 'Centro', 'Apto 101', 'São Paulo', 'SP'),
('54321-876', 'Avenida Brasil', '456', 'Jardins', 'Casa 2', 'Rio de Janeiro', 'RJ'),
('98765-432', 'Rua das Palmeiras', '789', 'Vila Nova', 'Sala 3', 'Belo Horizonte', 'MG');
('87654-321', 'Rua do Sol', '10', 'Boa Vista', 'Bloco A', 'Recife', 'PE'),
('34567-890', 'Avenida Paulista', '1000', 'Bela Vista', 'Andar 5', 'São Paulo', 'SP'),
('65432-109', 'Rua da Praia', '200', 'Centro', 'Loja 3', 'Porto Alegre', 'RS'),
('76543-210', 'Rua das Estrelas', '300', 'Jardim América', 'Cobertura', 'Curitiba', 'PR'),
('43210-987', 'Rua dos Pinheiros', '400', 'Pinheiros', 'Apto 502', 'São Paulo', 'SP'),
('11223-344', 'Rua do Comércio', '500', 'Centro', 'Sala 10', 'Salvador', 'BA'),
('55667-788', 'Rua das Pedras', '600', 'Barra', 'Casa 5', 'Rio de Janeiro', 'RJ'),
('99887-766', 'Rua das Árvores', '700', 'Alphaville', 'Casa 10', 'Campinas', 'SP'),
('12121-212', 'Rua dos Ventos', '800', 'Vila Mariana', 'Apto 301', 'São Paulo', 'SP'),
('34343-434', 'Rua das Montanhas', '900', 'Morumbi', 'Casa 15', 'São Paulo', 'SP');

-- Inserções na tabela cliente
INSERT INTO cliente (nome, idade, cpf, profissao, endereco_id)
VALUES
('João Silva', 30, '123.456.789-00', 'Engenheiro', 1),
('Maria Oliveira', 25, '987.654.321-00', 'Médica', 2),
('Carlos Souza', 40, '456.789.123-00', 'Advogado', 3),
('Ana Pereira', 28, '111.222.333-44', 'Designer', 4),
('Pedro Almeida', 35, '222.333.444-55', 'Professor', 5),
('Julia Santos', 22, '333.444.555-66', 'Estudante', 6),
('Lucas Ferreira', 29, '444.555.666-77', 'Desenvolvedor', 7),
('Mariana Costa', 31, '555.666.777-88', 'Advogada', 8),
('Fernando Lima', 42, '666.777.888-99', 'Empresário', 9),
('Camila Rodrigues', 27, '777.888.999-00', 'Jornalista', 10),
('Rafael Silva', 33, '888.999.000-11', 'Mecânico', 11),
('Isabela Oliveira', 26, '999.000.111-22', 'Psicóloga', 12),
('Gabriel Sousa', 38, '000.111.222-33', 'Arquiteto', 13);