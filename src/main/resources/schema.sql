
CREATE TABLE Product (
                         product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10, 2) NOT NULL,
                         quantity_in_stock INT NOT NULL
);


CREATE TABLE Customer (
                          customer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          contact_number VARCHAR(20) NOT NULL
);


CREATE TABLE `Order` (
                         order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         customer_id BIGINT,
                         order_date DATE NOT NULL,
                         shipping_address TEXT NOT NULL,
                         total_price DECIMAL(10, 2) NOT NULL,
                         order_status VARCHAR(50) NOT NULL,
                         FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

CREATE TABLE Order_Product (
                               order_id BIGINT,
                               product_id BIGINT,
                               PRIMARY KEY (order_id, product_id),
                               FOREIGN KEY (order_id) REFERENCES `Order`(order_id),
                               FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

INSERT INTO Product (name, description, price, quantity_in_stock)
VALUES ('Ноутбук', 'Игровой', 150000.00, 10),
       ('Смартфон Galaxy', 'Последняя модель', 80000.00, 25);
INSERT INTO Customer (first_name, last_name, email, contact_number)
VALUES ('Иван', 'Иванов', 'ivan.ivanov@example.com', '89161234567'),
       ('Мария', 'Петрова', 'maria.petrova@example.com', '89051234567');
INSERT INTO `Order` (customer_id, order_date, shipping_address, total_price, order_status)
VALUES (1, '2024-10-20', 'ул. Ленина, д. 1, Москва', 230000.00, 'Оплачен'),
       (2, '2024-10-21', 'ул. Пушкина, д. 10, Санкт-Петербург', 80000.00, 'Ожидается');
INSERT INTO Order_Product (order_id, product_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);

