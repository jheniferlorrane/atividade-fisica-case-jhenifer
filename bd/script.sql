CREATE DATABASE IF NOT EXISTS atividade;
USE atividade;

CREATE TABLE IF NOT EXISTS atividade (
    id_atividade BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único da atividade',
    funcional VARCHAR(50) NOT NULL COMMENT 'Código funcional do funcionário',
    data_hora DATETIME NOT NULL COMMENT 'Data e hora da atividade',
    codigo_atividade VARCHAR(20) NOT NULL COMMENT 'Tipo da atividade física',
    descricao_atividade VARCHAR(255) NOT NULL COMMENT 'Descrição detalhada'
);