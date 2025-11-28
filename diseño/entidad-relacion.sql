-- Diagrama Entidad-Relación de Prueba técnica
-- Para visualizar: https://dbdiagram.io/

Table users {
  id bigint [pk, increment]
  name varchar(255) [not null]
  email varchar(255) [unique, not null]
  password varchar(255) [not null, note: 'Hash encriptado']
  role varchar(20) [not null, default: 'USER', note: 'ADMIN o USER']
  created_at timestamp [default: `now()`]
  updated_at timestamp [default: `now()`]

  indexes {
    email
    role
  }
}

Table products {
  id bigint [pk, increment]
  sku varchar(50) [not null, unique, note: 'Stock Keeping Unit - Identificador único']
  name varchar(255) [not null]
  price decimal(10,2) [not null, note: 'Precio mayor o igual a 0']
  brand varchar(100) [not null]
  description text [null]
  created_at timestamp [default: `now()`]
  updated_at timestamp [default: `now()`]

  indexes {
    name
    brand
    price
  }
}



Table product_audit_logs {
  log_id bigint [pk, increment]
  product_id varchar(50) [not null]
  admin_user_id bigint [not null]
  action varchar(20) [not null, note: 'CREATE, UPDATE, DELETE']
  changes text [null, note: 'Descripción detallada de cambios (ej. precio: 100 → 150)']
  changed_at timestamp [default: `now()`]

  indexes {
    product_id
    admin_user_id
    changed_at
    action
  }
}


Ref: product_audit_logs.product_id > products.id [delete: cascade]
Ref: product_audit_logs.admin_user_id > users.id [delete: set null]
