-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 31, 2020 at 02:34 PM
-- Server version: 5.7.26
-- PHP Version: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `reactorsdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `adherance`
--

DROP TABLE IF EXISTS `adherance`;
CREATE TABLE IF NOT EXISTS `adherance`
(
    `user_id`        int(11)                              NOT NULL,
    `association_id` int(11)                              NOT NULL,
    `joined`         datetime                             NOT NULL,
    `fonction`       varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `id`             int(11)                              NOT NULL AUTO_INCREMENT,
    `description`    varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `role`           int(11)                              NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_2819DB33A76ED395` (`user_id`),
    KEY `IDX_2819DB33EFB9C8A5` (`association_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Dumping data for table `adherance`
--

INSERT INTO `adherance` (`user_id`, `association_id`, `joined`, `fonction`, `id`, `description`, `role`)
VALUES (74, 20, '2020-02-27 00:00:00', 'TESTING UPDATE', 9, 'Fondée lassociation', 1);

-- --------------------------------------------------------

--
-- Table structure for table `association`
--

DROP TABLE IF EXISTS `association`;
CREATE TABLE IF NOT EXISTS `association`
(
    `id`                 int(11)                             NOT NULL AUTO_INCREMENT,
    `domaine_id`         int(11)                              DEFAULT NULL,
    `id_manager`         int(11)                              DEFAULT NULL,
    `nom`                varchar(30) COLLATE utf8_unicode_ci NOT NULL,
    `telephone`          int(11)                             NOT NULL,
    `horaire_travail`    varchar(20) COLLATE utf8_unicode_ci NOT NULL,
    `photo_agence`       text COLLATE utf8_unicode_ci        NOT NULL,
    `piece_justificatif` text COLLATE utf8_unicode_ci        NOT NULL,
    `rue`                varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
    `code_postal`        int(11)                              DEFAULT NULL,
    `ville`              varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
    `latitude`           double                               DEFAULT NULL,
    `longitude`          double                               DEFAULT NULL,
    `approuved`          tinyint(1)                          NOT NULL,
    `description`        varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_FD8521CC4272FC9F` (`domaine_id`),
    KEY `id_manager` (`id_manager`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Dumping data for table `association`
--

INSERT INTO `association` (`id`, `domaine_id`, `id_manager`, `nom`, `telephone`, `horaire_travail`, `photo_agence`,
                           `piece_justificatif`, `rue`, `code_postal`, `ville`, `latitude`, `longitude`, `approuved`,
                           `description`)
VALUES (20, 76, 74, 'Enactus', 71268147, '8 vers 17', 'enactus.jpeg', 'enactus.docx', '45 rue bab jedid', 2000,
        'Nabeul', 0, 0, 1, 'azeazeazeaze'),
       (21, 139, 73, 'Lions', 25478985, 'De 6:00 Vers 8:00', 'enactus.jpeg', 'enactus.docx', '45 Rue elsalema', 200,
        'Tunis', 20, 20, 0,
        'Notre Mission. ENACTUS est une organisation non gouvernementale internationale à but non lucratif, créée en 1975 aux USA. ... ENACTUS accompagne les étudiants dans la mise en oeuvre de projets d\'entrepreneuriat social avec l\'implication de professionnels de l\'entreprise et du corps enseignant.');

-- --------------------------------------------------------

--
-- Table structure for table `association_user`
--

DROP TABLE IF EXISTS `association_user`;
CREATE TABLE IF NOT EXISTS `association_user`
(
    `association_id` int(11) NOT NULL,
    `user_id`        int(11) NOT NULL,
    PRIMARY KEY (`association_id`, `user_id`),
    KEY `IDX_A2312D48EFB9C8A5` (`association_id`),
    KEY `IDX_A2312D48A76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Dumping data for table `association_user`
--

INSERT INTO `association_user` (`association_id`, `user_id`)
VALUES (16, 42),
       (16, 43);

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category`
(
    `id`          int(11)                             NOT NULL AUTO_INCREMENT,
    `name`        varchar(60) COLLATE utf8_unicode_ci NOT NULL,
    `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 144
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`, `description`)
VALUES (76, 'Humanitaire', 'azeaze'),
       (133, 'added', 'added'),
       (138, 'testaazeazezeaze', 'aaaaaaaaaaa'),
       (139, 'azeazeazeaze', 'eazeazeaze'),
       (141, 'azeazeazeazeazeaze', 'eazeazeazeazeazeazeaze'),
       (142, 'azeazeazeaze', 'eazeazeaze'),
       (143, 'Sanitaire', 'Domaine de dra chnya');

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment`
(
    `id`             int(11)                          NOT NULL AUTO_INCREMENT,
    `user_id`        int(11)                          NOT NULL,
    `hebergement_id` int(11)                          NOT NULL,
    `content`        longtext COLLATE utf8_unicode_ci NOT NULL,
    `creation_date`  date                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_9474526CA76ED395` (`user_id`),
    KEY `IDX_9474526C23BB0F66` (`hebergement_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
CREATE TABLE IF NOT EXISTS `comments`
(
    `id`        int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`   int(11)                              NOT NULL,
    `thread_id` int(11)                              NOT NULL,
    `content`   varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `date`      date                                 NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_5F9E962AA76ED395` (`user_id`),
    KEY `IDX_5F9E962AE2904019` (`thread_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `demande`
--

DROP TABLE IF EXISTS `demande`;
CREATE TABLE IF NOT EXISTS `demande`
(
    `id`           int(11)                              NOT NULL AUTO_INCREMENT,
    `domaine_id`   int(11) DEFAULT NULL,
    `user_id`      int(11)                              NOT NULL,
    `title`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `description`  varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `address`      varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `state`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `phone`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `ups`          int(11)                              NOT NULL,
    `creationDate` date                                 NOT NULL,
    `rib`          varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `latitude`     varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `longitude`    varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `image`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_2694D7A54272FC9F` (`domaine_id`),
    KEY `IDX_2694D7A5A76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `don`
--

DROP TABLE IF EXISTS `don`;
CREATE TABLE IF NOT EXISTS `don`
(
    `id`           int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`      int(11)                              NOT NULL,
    `domaine_id`   int(11) DEFAULT NULL,
    `Title`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `Description`  varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `address`      varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `state`        int(11)                              NOT NULL,
    `phone`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `ups`          int(11)                              NOT NULL,
    `creationDate` date                                 NOT NULL,
    `latitude`     varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `longitude`    varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `image`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_F8F081D9A76ED395` (`user_id`),
    KEY `IDX_F8F081D94272FC9F` (`domaine_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `forum_category`
--

DROP TABLE IF EXISTS `forum_category`;
CREATE TABLE IF NOT EXISTS `forum_category`
(
    `id`          int(11)                              NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `describtion` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gouvernerat`
--

DROP TABLE IF EXISTS `gouvernerat`;
CREATE TABLE IF NOT EXISTS `gouvernerat`
(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hebergement`
--

DROP TABLE IF EXISTS `hebergement`;
CREATE TABLE IF NOT EXISTS `hebergement`
(
    `id`            int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`       int(11)                              NOT NULL,
    `description`   varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `governorat`    varchar(50) COLLATE utf8_unicode_ci  NOT NULL,
    `nbr_rooms`     int(11)                              NOT NULL,
    `duration`      int(11)                              NOT NULL,
    `creation_date` date                                 NOT NULL,
    `state`         int(11)                              NOT NULL,
    `telephone`     varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `image`         varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_4852DD9CA76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hebergement_request`
--

DROP TABLE IF EXISTS `hebergement_request`;
CREATE TABLE IF NOT EXISTS `hebergement_request`
(
    `id`              int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`         int(11)                              NOT NULL,
    `description`     longtext COLLATE utf8_unicode_ci     NOT NULL,
    `region`          varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `state`           int(11)                              NOT NULL,
    `native_country`  varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `arrival_date`    date                                 NOT NULL,
    `passport_number` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `civil_status`    int(11)                              NOT NULL,
    `children_number` int(11)                              NOT NULL,
    `name`            varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `telephone`       varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `creation_date`   date                                 NOT NULL,
    `is_anonymous`    int(11)                              NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_D42117ADA76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
CREATE TABLE IF NOT EXISTS `image`
(
    `id`             int(11)                              NOT NULL AUTO_INCREMENT,
    `hebergement_id` int(11)                              NOT NULL,
    `image`          varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_C53D045F23BB0F66` (`hebergement_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `imagedon`
--

DROP TABLE IF EXISTS `imagedon`;
CREATE TABLE IF NOT EXISTS `imagedon`
(
    `id`         int(11)                              NOT NULL AUTO_INCREMENT,
    `don_id`     int(11) DEFAULT NULL,
    `demande_id` int(11) DEFAULT NULL,
    `image`      varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_9B5927647B3C9061` (`don_id`),
    KEY `IDX_9B59276480E95E18` (`demande_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
CREATE TABLE IF NOT EXISTS `invitation`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT,
    `id_mission`      int(11)                                                       DEFAULT NULL,
    `id_notification` int(11)                                                       DEFAULT NULL,
    `id_user`         int(11)                                                       DEFAULT NULL,
    `etat`            enum ('inviter','accepter','réfuser') COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQ_F11D61A29C9503B8` (`id_notification`),
    KEY `IDX_F11D61A24EFA5B4C` (`id_mission`),
    KEY `IDX_F11D61A26B3CA4B` (`id_user`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mission`
--

DROP TABLE IF EXISTS `mission`;
CREATE TABLE IF NOT EXISTS `mission`
(
    `id`           int(11)                              NOT NULL AUTO_INCREMENT,
    `domaine_id`   int(11) DEFAULT NULL,
    `TitleMission` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `picture`      text COLLATE utf8_unicode_ci         NOT NULL,
    `description`  text COLLATE utf8_unicode_ci         NOT NULL,
    `location`     varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `sumCollected` double                               NOT NULL,
    `objectif`     double                               NOT NULL,
    `DateCreation` date                                 NOT NULL,
    `ups`          int(11)                              NOT NULL,
    `DateFin`      date                                 NOT NULL,
    `latitude`     double  DEFAULT NULL,
    `longitude`    double  DEFAULT NULL,
    `CreatedBy`    int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_9067F23C4272FC9F` (`domaine_id`),
    KEY `IDX_9067F23C51A7C4E1` (`CreatedBy`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mission_donation`
--

DROP TABLE IF EXISTS `mission_donation`;
CREATE TABLE IF NOT EXISTS `mission_donation`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `id_user`      int(11) DEFAULT NULL,
    `id_mission`   int(11) DEFAULT NULL,
    `somme_donner` double  NOT NULL,
    `dateDonation` date    NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_391980196B3CA4B` (`id_user`),
    KEY `IDX_391980194EFA5B4C` (`id_mission`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification`
(
    `id`                int(11)                              NOT NULL AUTO_INCREMENT,
    `id_user`           int(11)                              DEFAULT NULL,
    `id_mission`        int(11)                              DEFAULT NULL,
    `id_association`    int(11)                              DEFAULT NULL,
    `title`             varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `description`       longtext COLLATE utf8_unicode_ci     NOT NULL,
    `icon`              varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `route`             varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `route_parameters`  longtext COLLATE utf8_unicode_ci COMMENT '(DC2Type:array)',
    `notification_date` datetime                             NOT NULL,
    `seen`              tinyint(1)                           NOT NULL,
    `id_invitation`     int(11)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQ_BF5476CA5497E175` (`id_invitation`),
    KEY `IDX_BF5476CA6B3CA4B` (`id_user`),
    KEY `IDX_BF5476CA4EFA5B4C` (`id_mission`),
    KEY `IDX_BF5476CAE597EC3B` (`id_association`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
CREATE TABLE IF NOT EXISTS `post_like`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `user_id`    int(11) DEFAULT NULL,
    `don_id`     int(11) DEFAULT NULL,
    `demande_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_653627B8A76ED395` (`user_id`),
    KEY `IDX_653627B87B3C9061` (`don_id`),
    KEY `IDX_653627B880E95E18` (`demande_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `refugee`
--

DROP TABLE IF EXISTS `refugee`;
CREATE TABLE IF NOT EXISTS `refugee`
(
    `id`               int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`          int(11)                              NOT NULL,
    `native_country`   varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `case_description` longtext COLLATE utf8_unicode_ci     NOT NULL,
    `arrival_date`     date                                 NOT NULL,
    `passport_number`  varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `civil_status`     int(11)                              NOT NULL,
    `children_number`  int(11)                              NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQ_9CFB8546A76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `requete`
--

DROP TABLE IF EXISTS `requete`;
CREATE TABLE IF NOT EXISTS `requete`
(
    `id`          int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`     int(11)                              NOT NULL,
    `rponse_id`   int(11) DEFAULT NULL,
    `Sujet`       varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `DernierMAJ`  datetime                             NOT NULL,
    `Statut`      int(11)                              NOT NULL,
    `Type`        int(11)                              NOT NULL,
    `Description` longtext COLLATE utf8_unicode_ci     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQ_1E6639AAF086BD47` (`rponse_id`),
    KEY `IDX_1E6639AAA76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rponse`
--

DROP TABLE IF EXISTS `rponse`;
CREATE TABLE IF NOT EXISTS `rponse`
(
    `id`         int(11)                          NOT NULL AUTO_INCREMENT,
    `user_id`    int(11)                          NOT NULL,
    `requete_id` int(11)                          NOT NULL,
    `Sujet`      longtext COLLATE utf8_unicode_ci NOT NULL,
    `Rep`        longtext COLLATE utf8_unicode_ci NOT NULL,
    `Date`       datetime                         NOT NULL,
    `Rating`     int(11)                          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQ_163C533B18FB544F` (`requete_id`),
    KEY `IDX_163C533BA76ED395` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `threads`
--

DROP TABLE IF EXISTS `threads`;
CREATE TABLE IF NOT EXISTS `threads`
(
    `id`           int(11)                              NOT NULL AUTO_INCREMENT,
    `user_id`      int(11)                              NOT NULL,
    `category_id`  int(11)                              NOT NULL,
    `title`        varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `content`      varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `Lastmodified` date                                 NOT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_6F8E3DDDA76ED395` (`user_id`),
    KEY `IDX_6F8E3DDD12469DE2` (`category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `up`
--

DROP TABLE IF EXISTS `up`;
CREATE TABLE IF NOT EXISTS `up`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `user_id`    int(11) DEFAULT NULL,
    `mission_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `IDX_4394EE70A76ED395` (`user_id`),
    KEY `IDX_4394EE70BE6CAE90` (`mission_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`                    int(11)                              NOT NULL AUTO_INCREMENT,
    `username`              varchar(180) COLLATE utf8_unicode_ci NOT NULL,
    `username_canonical`    varchar(180) COLLATE utf8_unicode_ci NOT NULL,
    `email`                 varchar(180) COLLATE utf8_unicode_ci NOT NULL,
    `email_canonical`       varchar(180) COLLATE utf8_unicode_ci NOT NULL,
    `enabled`               tinyint(1)                           NOT NULL,
    `salt`                  varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `password`              varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `last_login`            datetime                             DEFAULT NULL,
    `confirmation_token`    varchar(180) COLLATE utf8_unicode_ci DEFAULT NULL,
    `password_requested_at` datetime                             DEFAULT NULL,
    `roles`                 longtext COLLATE utf8_unicode_ci     NOT NULL COMMENT '(DC2Type:array)',
    `nom`                   varchar(20) COLLATE utf8_unicode_ci  DEFAULT NULL,
    `prenom`                varchar(20) COLLATE utf8_unicode_ci  DEFAULT NULL,
    `date_naissance`        date                                 DEFAULT NULL,
    `telephone`             int(11)                              DEFAULT NULL,
    `adresse`               varchar(50) COLLATE utf8_unicode_ci  DEFAULT NULL,
    `approuved`             tinyint(1)                           DEFAULT NULL,
    `banned`                tinyint(1)                           NOT NULL,
    `image`                 text COLLATE utf8_unicode_ci,
    `cin`                   varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `is_admin`              tinyint(1)                           DEFAULT NULL,
    `is_ass_admin`          tinyint(1)                           DEFAULT NULL,
    `password_plain`        varchar(120) COLLATE utf8_unicode_ci DEFAULT NULL,
    `is_member`             tinyint(1)                           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQ_8D93D64992FC23A8` (`username_canonical`),
    UNIQUE KEY `UNIQ_8D93D649A0D96FBF` (`email_canonical`),
    UNIQUE KEY `UNIQ_8D93D649C05FB297` (`confirmation_token`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 80
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `username_canonical`, `email`, `email_canonical`, `enabled`, `salt`, `password`,
                    `last_login`, `confirmation_token`, `password_requested_at`, `roles`, `nom`, `prenom`,
                    `date_naissance`, `telephone`, `adresse`, `approuved`, `banned`, `image`, `cin`, `is_admin`,
                    `is_ass_admin`, `password_plain`, `is_member`)
VALUES (72, 'adminuser1', 'adminuser1', 'admin@reactors.tn', 'admin@reactors.tn', 1, NULL,
        '$2y$13$kPwcufWkmi/Gb6TVlx2DTOY9vF.dHNbfmXski6oBUEwny0ikWwtXO', '2020-03-03 00:00:00', NULL, NULL,
        'a:1:{i:0;s:16:\"ROLE_SUPER_ADMIN\";}', 'Chihab', 'Hajji', '2020-03-10', NULL, NULL, 1, 0, 'user.png',
        '09632013', NULL, NULL, NULL, NULL),
       (73, 'user', 'user', 'user@reactors.tn', 'user@reactors.tn', 1, NULL,
        '$2y$13$tP8GcF7YwV/dB0j9LO0oieWxQXqx24W9PZBH/RQ6WLYmpPMF2Gc6S', '2020-03-03 00:00:00', NULL, NULL,
        'a:1:{i:0;s:11:\"ROLE_CLIENT\";}', 'Chihab', 'Hajji', '2020-03-17', NULL, NULL, 1, 0, 'user.png', NULL, NULL,
        NULL, NULL, NULL),
       (74, 'assadmin', 'assadmin', 'admin@enactus.tn', 'admin@enactus.tn', 1, NULL,
        '$2y$13$06u5uSPoVQplVn/n.o8LWuuyqIwAItQejvrFXMASY0ChV7ixWMIoO', '2020-02-27 10:47:51', NULL, NULL,
        'a:1:{i:0;s:22:\"ROLE_ASSOCIATION_ADMIN\";}', 'Hajji', 'Chihab', '2020-03-18', NULL, NULL, 1, 0, 'user.png',
        NULL, NULL, NULL, NULL, NULL),
       (75, 'test', 'test', 'test@test.tn', 'test@test.tn', 1, NULL,
        '$2y$13$JUFUU46O3lE6HF76UjNNAugE0SMKudpsmPeFlvQXJZfRFz0/n28IO', '2020-03-02 10:48:40', NULL, NULL,
        'a:1:{i:0;s:11:\"ROLE_CLIENT\";}', 'chihab', 'hajji', '2017-03-03', 96320, 'azeaze', 1, 0, 'user.png', NULL,
        NULL, NULL, NULL, NULL),
       (76, 'yfzyafrr', 'yfzyafrr', 'yfz@ufz.com', 'yfz@ufz.com', 1, NULL,
        '$2y$13$E9K1yJfBotlReYYVL1dQBeJGVIGiBLuGRbLImnK.f1bqjaPVAVLFG', '2020-03-16 22:18:23', NULL, NULL,
        'a:1:{i:0;s:11:\"ROLE_CLIENT\";}', 'chihab', 'hajji', '2019-04-04', 9632013, 'azeaze', 1, 0, 'user.png', NULL,
        NULL, NULL, NULL, NULL),
       (78, 'chih', 'chih', 'aze@po.com', 'aze@po.com', 1, NULL,
        '$2y$13$kQFsyhtsAiCNGJydA5doaOH31J46cUZ4VZCXZVeZuJqOgeUCppB82', '2020-03-16 22:54:58', NULL, NULL,
        'a:1:{i:0;s:11:\"ROLE_CLIENT\";}', 'chihab', 'hajji', '2018-04-03', 29164666, 'aez', 1, 0, 'user.png', NULL,
        NULL, NULL, NULL, NULL),
       (79, 'pol', 'pol', 'pol@pol.com', 'pol@pol.com', 1, NULL,
        '$2y$13$Ai7Qi1tCXg3R1np4RedabO9L3rZ5bNAbl3xBBeTutNGJY54U2PxLS', '2020-03-16 23:00:21', NULL, NULL,
        'a:1:{i:0;s:11:\"ROLE_CLIENT\";}', 'caze', 'eaze', '2018-04-03', 29164666, 'azea', 1, 0, 'user.png', NULL, NULL,
        NULL, NULL, NULL);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `adherance`
--
ALTER TABLE `adherance`
    ADD CONSTRAINT `FK_2819DB33A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_2819DB33EFB9C8A5` FOREIGN KEY (`association_id`) REFERENCES `association` (`id`);

--
-- Constraints for table `association`
--
ALTER TABLE `association`
    ADD CONSTRAINT `FK_FD8521CC24B98CC9` FOREIGN KEY (`id_manager`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_FD8521CC4272FC9F` FOREIGN KEY (`domaine_id`) REFERENCES `category` (`id`);

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
    ADD CONSTRAINT `FK_9474526C23BB0F66` FOREIGN KEY (`hebergement_id`) REFERENCES `hebergement` (`id`),
    ADD CONSTRAINT `FK_9474526CA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
    ADD CONSTRAINT `FK_5F9E962AA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_5F9E962AE2904019` FOREIGN KEY (`thread_id`) REFERENCES `threads` (`id`);

--
-- Constraints for table `demande`
--
ALTER TABLE `demande`
    ADD CONSTRAINT `FK_2694D7A54272FC9F` FOREIGN KEY (`domaine_id`) REFERENCES `category` (`id`),
    ADD CONSTRAINT `FK_2694D7A5A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `don`
--
ALTER TABLE `don`
    ADD CONSTRAINT `FK_F8F081D94272FC9F` FOREIGN KEY (`domaine_id`) REFERENCES `category` (`id`),
    ADD CONSTRAINT `FK_F8F081D9A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `hebergement`
--
ALTER TABLE `hebergement`
    ADD CONSTRAINT `FK_4852DD9CA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `hebergement_request`
--
ALTER TABLE `hebergement_request`
    ADD CONSTRAINT `FK_D42117ADA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `image`
--
ALTER TABLE `image`
    ADD CONSTRAINT `FK_C53D045F23BB0F66` FOREIGN KEY (`hebergement_id`) REFERENCES `hebergement` (`id`);

--
-- Constraints for table `imagedon`
--
ALTER TABLE `imagedon`
    ADD CONSTRAINT `FK_9B5927647B3C9061` FOREIGN KEY (`don_id`) REFERENCES `don` (`id`),
    ADD CONSTRAINT `FK_9B59276480E95E18` FOREIGN KEY (`demande_id`) REFERENCES `demande` (`id`);

--
-- Constraints for table `invitation`
--
ALTER TABLE `invitation`
    ADD CONSTRAINT `FK_F11D61A24EFA5B4C` FOREIGN KEY (`id_mission`) REFERENCES `mission` (`id`) ON DELETE SET NULL,
    ADD CONSTRAINT `FK_F11D61A26B3CA4B` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_F11D61A29C9503B8` FOREIGN KEY (`id_notification`) REFERENCES `notification` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `mission`
--
ALTER TABLE `mission`
    ADD CONSTRAINT `FK_9067F23C4272FC9F` FOREIGN KEY (`domaine_id`) REFERENCES `category` (`id`),
    ADD CONSTRAINT `FK_9067F23C51A7C4E1` FOREIGN KEY (`CreatedBy`) REFERENCES `user` (`id`);

--
-- Constraints for table `mission_donation`
--
ALTER TABLE `mission_donation`
    ADD CONSTRAINT `FK_391980194EFA5B4C` FOREIGN KEY (`id_mission`) REFERENCES `mission` (`id`) ON DELETE SET NULL,
    ADD CONSTRAINT `FK_391980196B3CA4B` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
    ADD CONSTRAINT `FK_BF5476CA4EFA5B4C` FOREIGN KEY (`id_mission`) REFERENCES `mission` (`id`),
    ADD CONSTRAINT `FK_BF5476CA5497E175` FOREIGN KEY (`id_invitation`) REFERENCES `invitation` (`id`) ON DELETE SET NULL,
    ADD CONSTRAINT `FK_BF5476CA6B3CA4B` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_BF5476CAE597EC3B` FOREIGN KEY (`id_association`) REFERENCES `association` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `post_like`
--
ALTER TABLE `post_like`
    ADD CONSTRAINT `FK_653627B87B3C9061` FOREIGN KEY (`don_id`) REFERENCES `don` (`id`),
    ADD CONSTRAINT `FK_653627B880E95E18` FOREIGN KEY (`demande_id`) REFERENCES `demande` (`id`),
    ADD CONSTRAINT `FK_653627B8A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `refugee`
--
ALTER TABLE `refugee`
    ADD CONSTRAINT `FK_9CFB8546A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `requete`
--
ALTER TABLE `requete`
    ADD CONSTRAINT `FK_1E6639AAA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_1E6639AAF086BD47` FOREIGN KEY (`rponse_id`) REFERENCES `rponse` (`id`);

--
-- Constraints for table `rponse`
--
ALTER TABLE `rponse`
    ADD CONSTRAINT `FK_163C533B18FB544F` FOREIGN KEY (`requete_id`) REFERENCES `requete` (`id`),
    ADD CONSTRAINT `FK_163C533BA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `threads`
--
ALTER TABLE `threads`
    ADD CONSTRAINT `FK_6F8E3DDD12469DE2` FOREIGN KEY (`category_id`) REFERENCES `forum_category` (`id`),
    ADD CONSTRAINT `FK_6F8E3DDDA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `up`
--
ALTER TABLE `up`
    ADD CONSTRAINT `FK_4394EE70A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `FK_4394EE70BE6CAE90` FOREIGN KEY (`mission_id`) REFERENCES `mission` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
