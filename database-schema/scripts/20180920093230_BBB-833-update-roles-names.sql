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

-- // BBB-833-update-roles-names
-- Migration SQL that makes the change goes here.
update usermanagement.roles set description = 'DfT Administrator' where id = 1;
update usermanagement.roles set description = 'Administrator' where id = 2;
update usermanagement.roles set description = 'Editor' where id = 3;
update usermanagement.roles set description = 'View only' where id = 4;



-- //@UNDO
-- SQL to undo the change goes here.
update usermanagement.roles set description = 'DfT Admin' where id = 1;
update usermanagement.roles set description = 'LA Admin' where id = 2;
update usermanagement.roles set description = 'LA Editor' where id = 3;
update usermanagement.roles set description = 'LA Read Only' where id = 4;


