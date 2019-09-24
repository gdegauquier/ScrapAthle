CREATE TABLE "event" (
                         "id" varchar PRIMARY KEY,
                         "code" varchar,
                         "name" varchar,
                         "is_labeled" boolean,
                         "is_technically_adviced" boolean,
                         "date_begin" date,
                         "date_end" date,
                         "conditions" varchar,
                         "other_info" varchar,
                         "town_id" int,
                         "league_id" int,
                         "level_id" int
);

CREATE TABLE "address_type" (
                                "id" int PRIMARY KEY,
                                "code" varchar,
                                "label" varchar
);

CREATE TABLE "address" (
                           "id" int PRIMARY KEY,
                           "line_1" varchar,
                           "line_2" varchar,
                           "line_3" varchar,
                           "town_id" int,
                           "address_type_id" int
);

CREATE TABLE "service" (
                           "id" int PRIMARY KEY,
                           "code" varchar,
                           "label" varchar
);

CREATE TABLE "level" (
                         "id" int PRIMARY KEY,
                         "code" varchar,
                         "label" varchar
);

CREATE TABLE "contact_type" (
                                "id" int PRIMARY KEY,
                                "code" varchar,
                                "label" varchar
);

CREATE TABLE "test" (
                        "id" int PRIMARY KEY,
                        "date_test" date,
                        "time_test" time,
                        "distance" varchar,
                        "label" varchar,
                        "description" varchar,
                        "amount" varchar,
                        "conditions" varchar,
                        "former_year" varchar,
                        "rewards" varchar
);

CREATE TABLE "test_category" (
                                 "id" int PRIMARY KEY,
                                 "code" varchar,
                                 "label" varchar
);

CREATE TABLE "rel_test_test_category" (
                                          "test_id" int,
                                          "test_category_id" int
);

CREATE TABLE "rel_event_test" (
                                  "test_id" int,
                                  "event_id" varchar
);

CREATE TABLE "contact" (
                           "id" int PRIMARY KEY,
                           "firstname" varchar,
                           "lastname" varchar,
                           "name" varchar,
                           "telephone_1" varchar,
                           "telephone_2" varchar,
                           "email" varchar,
                           "contact_type_id" int
);

CREATE TABLE "rel_event_contact" (
                                     "contact_id" int,
                                     "event_id" varchar
);

CREATE TABLE "rel_event_service" (
                                     "service_id" int,
                                     "event_id" varchar
);

CREATE TABLE "rel_event_address" (
                                     "event_id" varchar,
                                     "address_id" int
);

CREATE TABLE "event_type" (
                              "id" int PRIMARY KEY,
                              "code" varchar,
                              "label" varchar,
                              "is_sub_type" boolean
);

CREATE TABLE "rel_event_type" (
                                  "event_id" varchar,
                                  "event_type_id" int
);

CREATE TABLE "town" (
                        "id" int PRIMARY KEY,
                        "name" varchar,
                        "zipcode" varchar,
                        "inseecode" varchar,
                        "department_id" int
);

CREATE TABLE "department" (
                              "id" int PRIMARY KEY,
                              "code" varchar
);

CREATE TABLE "league" (
                          "id" int PRIMARY KEY,
                          "code" varchar,
                          "label" varchar
);

ALTER TABLE "event" ADD FOREIGN KEY ("town_id") REFERENCES "town" ("id");

ALTER TABLE "event" ADD FOREIGN KEY ("league_id") REFERENCES "league" ("id");

ALTER TABLE "event" ADD FOREIGN KEY ("level_id") REFERENCES "level" ("id");

ALTER TABLE "address" ADD FOREIGN KEY ("town_id") REFERENCES "town" ("id");

ALTER TABLE "address" ADD FOREIGN KEY ("address_type_id") REFERENCES "address_type" ("id");

ALTER TABLE "rel_test_test_category" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("id");

ALTER TABLE "rel_test_test_category" ADD FOREIGN KEY ("test_category_id") REFERENCES "test_category" ("id");

ALTER TABLE "rel_event_test" ADD FOREIGN KEY ("test_id") REFERENCES "test" ("id");

ALTER TABLE "rel_event_test" ADD FOREIGN KEY ("event_id") REFERENCES "event" ("id");

ALTER TABLE "contact" ADD FOREIGN KEY ("contact_type_id") REFERENCES "contact_type" ("id");

ALTER TABLE "rel_event_contact" ADD FOREIGN KEY ("contact_id") REFERENCES "contact" ("id");

ALTER TABLE "rel_event_contact" ADD FOREIGN KEY ("event_id") REFERENCES "event" ("id");

ALTER TABLE "rel_event_service" ADD FOREIGN KEY ("service_id") REFERENCES "service" ("id");

ALTER TABLE "rel_event_service" ADD FOREIGN KEY ("event_id") REFERENCES "event" ("id");

ALTER TABLE "rel_event_address" ADD FOREIGN KEY ("event_id") REFERENCES "event" ("id");

ALTER TABLE "rel_event_address" ADD FOREIGN KEY ("address_id") REFERENCES "address" ("id");

ALTER TABLE "rel_event_type" ADD FOREIGN KEY ("event_id") REFERENCES "event" ("id");

ALTER TABLE "rel_event_type" ADD FOREIGN KEY ("event_type_id") REFERENCES "event_type" ("id");

ALTER TABLE "town" ADD FOREIGN KEY ("department_id") REFERENCES "department" ("id");
