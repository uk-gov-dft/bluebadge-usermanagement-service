--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // BBB-804-client-credentials
-- Migration SQL that makes the change goes here.

CREATE TABLE usermanagement.client_credentials(
    client_id  VARCHAR(50) PRIMARY KEY,
    client_secret  VARCHAR(100) not null,
    local_authority_short_code VARCHAR(10) not null,
    active BOOLEAN not null,
    creation_timestamp TIMESTAMP not null,
    expiry_timestamp TIMESTAMP not null
);

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE IF EXISTS usermanagement.client_credentials;

