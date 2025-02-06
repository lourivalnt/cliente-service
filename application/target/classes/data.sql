-- Inserções na tabela endereco
INSERT INTO endereco (cep, rua, numero, bairro, complemento, cidade, uf)
VALUES
('12345-678', 'Rua das Flores', '123', 'Centro', 'Apto 101', 'São Paulo', 'SP'),
('54321-876', 'Avenida Brasil', '456', 'Jardins', 'Casa 2', 'Rio de Janeiro', 'RJ'),
('98765-432', 'Rua das Palmeiras', '789', 'Vila Nova', 'Sala 3', 'Belo Horizonte', 'MG');

-- Inserções na tabela cliente
INSERT INTO cliente (nome, idade, cpf, profissao, endereco_id)
VALUES
('João Silva', 30, '123.456.789-00', 'Engenheiro', 1),
('Maria Oliveira', 25, '987.654.321-00', 'Médica', 2),
('Carlos Souza', 40, '456.789.123-00', 'Advogado', 3);