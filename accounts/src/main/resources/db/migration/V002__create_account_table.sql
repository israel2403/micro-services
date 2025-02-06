CREATE TABLE IF NOT EXISTS `account` (
    `id` UUID NOT NULL,
    `customer_id` UUID NOT NULL,
    `account_number` int,
    `account_type` varchar(100) NOT NULL,
    `branch_address` varchar(200) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
);