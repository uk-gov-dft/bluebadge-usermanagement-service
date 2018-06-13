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

--// 20180427143323_BBB-17-CreateUser
-- Migration SQL that makes the change goes here.
CREATE TABLE local_authority_area(
    id VARCHAR(20) PRIMARY KEY
);

COMMENT ON TABLE local_authority_area IS 'Valid areas e.g. England or Scotland.';
COMMENT ON COLUMN local_authority_area.id IS 'The area identifier.  e.g. England';

INSERT INTO local_authority_area (id)
VALUES ('England'), ('Wales'), ('Scotland'), ('Northern Ireland');

CREATE TABLE local_authority (
    id INTEGER PRIMARY KEY,
    local_authority_area_id VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL
);

ALTER TABLE local_authority
    ADD CONSTRAINT local_authority_local_authority_area_id_fk
    FOREIGN KEY (local_authority_area_id)
    REFERENCES local_authority_area(id);

COMMENT ON TABLE local_authority IS 'UK local authorities that require login to system';
COMMENT ON COLUMN local_authority.id IS 'PK NOT auto incremented.';
COMMENT ON COLUMN local_authority.local_authority_area_id IS 'e.g. England or Scotland.';
COMMENT ON COLUMN local_authority.name IS 'Unique name of the local authority';

INSERT INTO local_authority (id, local_authority_area_id, name)
VALUES
 (1, 'Scotland', 'Aberdeen City Council')
,(2, 'Scotland', 'Aberdeenshire Council')
,(3, 'Wales', 'Anglesey')
,(4, 'Scotland', 'Angus Council')
,(5, 'Scotland', 'Argyll and Bute Council')
,(6, 'England', 'Barking and Dagenham')
,(7, 'England', 'Barnet')
,(8, 'England', 'Barnsley')
,(9, 'England', 'Bath & NE Somerset')
,(10, 'England', 'Bedford')
,(11, 'England', 'Bexley')
,(12, 'England', 'Birmingham')
,(13, 'England', 'Blackburn with Darwen')
,(14, 'England', 'Blackpool')
,(15, 'Wales', 'Blaenau-gwent')
,(16, 'England', 'Bolton')
,(17, 'England', 'Bournemouth')
,(18, 'England', 'Bracknell Forest')
,(19, 'England', 'Bradford')
,(20, 'England', 'Brent')
,(21, 'Wales', 'Bridgend')
,(22, 'England', 'Brighton & Hove')
,(23, 'England', 'Bristol')
,(24, 'England', 'Bromley')
,(25, 'England', 'Buckinghamshire')
,(26, 'England', 'Bury')
,(27, 'Wales', 'Caerphilly')
,(28, 'England', 'Calderdale')
,(29, 'England', 'Cambridgeshire')
,(30, 'England', 'Camden')
,(31, 'Wales', 'Cardiff')
,(32, 'Wales', 'Carmarthenshire')
,(33, 'England', 'Central Bedfordshire')
,(34, 'Wales', 'Ceredigion')
,(35, 'England', 'Cheshire East')
,(36, 'England', 'Cheshire West and Chester')
,(37, 'England', 'City of London')
,(38, 'Scotland', 'Clackmannanshire Council')
,(39, 'Wales', 'Conwy')
,(40, 'England', 'Cornwall')
,(41, 'England', 'Coventry')
,(42, 'England', 'Croydon')
,(43, 'England', 'Cumbria')
,(44, 'England', 'Darlington')
,(45, 'Wales', 'Denbighshire')
,(46, 'England', 'Derby')
,(47, 'England', 'Derbyshire')
,(48, 'England', 'Devon')
,(49, 'England', 'Doncaster')
,(50, 'England', 'Dorset')
,(51, 'England', 'Dudley')
,(52, 'Scotland', 'Dumfries and Galloway Council')
,(53, 'Scotland', 'Dundee City Council')
,(54, 'England', 'Durham')
,(55, 'England', 'Ealing')
,(56, 'Scotland', 'East Ayrshire Council')
,(57, 'Scotland', 'East Dunbartonshire Council')
,(58, 'Scotland', 'East Lothian District Council')
,(59, 'Scotland', 'East Renfrewshire Council')
,(60, 'England', 'East Riding of Yorkshire')
,(61, 'England', 'East Sussex')
,(62, 'Scotland', 'Edinburgh City Council')
,(63, 'Scotland', 'Eilean Siar')
,(64, 'England', 'Enfield')
,(65, 'England', 'Essex')
,(66, 'Scotland', 'Falkirk Council')
,(67, 'Scotland', 'Fife Council')
,(68, 'Wales', 'Flintshire')
,(69, 'England', 'Gateshead')
,(70, 'Scotland', 'Glasgow City Council')
,(71, 'England', 'Gloucestershire')
,(72, 'England', 'Greenwich')
,(73, 'Wales', 'Gwynedd ')
,(74, 'England', 'Hackney')
,(75, 'England', 'Halton')
,(76, 'England', 'Hammersmith and Fulham')
,(77, 'England', 'Hampshire')
,(78, 'England', 'Haringey')
,(79, 'England', 'Harrow')
,(80, 'England', 'Hartlepool')
,(81, 'England', 'Havering')
,(82, 'England', 'Herefordshire')
,(83, 'England', 'Hertfordshire')
,(84, 'Scotland', 'Highland Council')
,(85, 'England', 'Hillingdon')
,(86, 'England', 'Hounslow')
,(87, 'England', 'Hull')
,(88, 'Scotland', 'Inverclyde Council')
,(89, 'England', 'Isle of Wight')
,(90, 'England', 'Isles of Scilly')
,(91, 'England', 'Islington')
,(92, 'England', 'Kensington and Chelsea')
,(93, 'England', 'Kent')
,(94, 'England', 'Kingston upon Thames')
,(95, 'England', 'Kirklees')
,(96, 'England', 'Knowsley')
,(97, 'England', 'Lambeth')
,(98, 'England', 'Lancashire')
,(99, 'England', 'Leeds')
,(100, 'England', 'Leicester')
,(101, 'England', 'Leicestershire')
,(102, 'England', 'Lewisham')
,(103, 'England', 'Lincolnshire')
,(104, 'England', 'Liverpool')
,(105, 'England', 'Luton')
,(106, 'England', 'Manchester')
,(107, 'England', 'Medway Towns')
,(108, 'Wales', 'Merthyr')
,(109, 'England', 'Merton')
,(110, 'England', 'Middlesbrough')
,(111, 'Scotland', 'Midlothian Council')
,(112, 'England', 'Milton Keynes')
,(113, 'Wales', 'Monmouthshire')
,(114, 'Scotland', 'Moray Council')
,(115, 'Wales', 'Neath-porttalbot')
,(116, 'England', 'Newcastle')
,(117, 'England', 'Newham')
,(118, 'Wales', 'Newport')
,(119, 'England', 'Norfolk')
,(120, 'Northern Ireland', 'Northern Ireland ')
,(121, 'Scotland', 'North Ayrshire Council')
,(122, 'England', 'North East Lincolnshire')
,(123, 'Scotland', 'North Lanarkshire')
,(124, 'England', 'North Lincolnshire')
,(125, 'England', 'North Somerset')
,(126, 'England', 'North Tyneside')
,(127, 'England', 'North Yorkshire')
,(128, 'England', 'Northamptonshire')
,(129, 'England', 'Northumberland')
,(130, 'England', 'Nottingham')
,(131, 'England', 'Nottinghamshire')
,(132, 'England', 'Oldham')
,(133, 'Scotland', 'Orkney Islands Council')
,(134, 'England', 'Oxfordshire')
,(135, 'Wales', 'Pembrokeshire')
,(136, 'Scotland', 'Perth and Kinross Council')
,(137, 'England', 'Peterborough')
,(138, 'England', 'Plymouth')
,(139, 'England', 'Poole')
,(140, 'England', 'Portsmouth')
,(141, 'Wales', 'Powys')
,(142, 'England', 'Reading')
,(143, 'England', 'Redbridge')
,(144, 'England', 'Redcar & Cleveland')
,(145, 'Scotland', 'Renfrewshire Council')
,(146, 'Wales', 'Rhondda-Cynon-Taff')
,(147, 'England', 'Richmond upon Thames')
,(148, 'England', 'Rochdale')
,(149, 'England', 'Rotherham')
,(150, 'England', 'Rutland')
,(151, 'England', 'Salford')
,(152, 'England', 'Sandwell')
,(153, 'Scotland', 'Scottish Borders Council')
,(154, 'England', 'Sefton')
,(155, 'England', 'Sheffield')
,(156, 'Scotland', 'Shetland Islands Council')
,(157, 'England', 'Shropshire')
,(158, 'England', 'Slough')
,(159, 'England', 'Solihull')
,(160, 'England', 'Somerset')
,(161, 'Scotland', 'South Ayrshire Council')
,(162, 'England', 'South Gloucestershire')
,(163, 'Scotland', 'South Lanarkshire Council')
,(164, 'England', 'South Tyneside')
,(165, 'England', 'Southampton')
,(166, 'England', 'Southend')
,(167, 'England', 'Southwark')
,(168, 'England', 'St. Helens')
,(169, 'England', 'Staffordshire')
,(170, 'Scotland', 'Stirling Council')
,(171, 'England', 'Stockport')
,(172, 'England', 'Stockton')
,(173, 'England', 'Stoke-on-Trent')
,(174, 'England', 'Suffolk')
,(175, 'England', 'Sunderland')
,(176, 'England', 'Surrey')
,(177, 'England', 'Sutton')
,(178, 'Wales', 'Swansea')
,(179, 'England', 'Swindon')
,(180, 'England', 'Tameside')
,(181, 'England', 'Telford and the Wrekin')
,(182, 'England', 'Thurrock')
,(183, 'England', 'Torbay')
,(184, 'Wales', 'Torfaen')
,(185, 'England', 'Tower Hamlets')
,(186, 'England', 'Trafford')
,(187, 'Wales', 'Vale of Glamorgan')
,(188, 'England', 'Wakefield')
,(189, 'England', 'Walsall')
,(190, 'England', 'Waltham Forest')
,(191, 'England', 'Wandsworth')
,(192, 'England', 'Warrington')
,(193, 'England', 'Warwickshire')
,(194, 'England', 'West Berkshire')
,(195, 'Scotland', 'West Dunbartonshire Council')
,(196, 'Scotland', 'West Lothian Council')
,(197, 'England', 'West Sussex')
,(198, 'England', 'Westminster')
,(199, 'England', 'Wigan')
,(200, 'England', 'Wiltshire')
,(201, 'England', 'Windsor & Maidenhead')
,(202, 'England', 'Wirral')
,(203, 'England', 'Wokingham')
,(204, 'England', 'Wolverhampton')
,(205, 'England', 'Worcestershire')
,(206, 'Wales', 'Wrexham')
,(207, 'England', 'York')
;

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    description VARCHAR(20) NOT NULL
);

COMMENT ON TABLE roles IS 'Assuming 1 role per user.';
COMMENT ON COLUMN roles.id IS 'PK. Auto generated.';
COMMENT ON COLUMN roles.description IS 'Description of role.';

INSERT INTO roles (description)
VALUES ('DfT Admin'), ('LA Admin')
, ('LA User'), ('LA Read Only'), ('Read Only');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    local_authority_id INTEGER,
    email_address VARCHAR(200) NOT NULL,
    forename VARCHAR(60),
    surname VARCHAR(60),
    role_id INTEGER
);

CREATE UNIQUE INDEX user_email_address_ux ON users(email_address);

ALTER TABLE users
    ADD CONSTRAINT users_local_authority_id_fk
    FOREIGN KEY (local_authority_id)
    REFERENCES local_authority(id);

ALTER TABLE users
    ADD CONSTRAINT users_role_id_fk
    FOREIGN KEY (role_id)
    REFERENCES roles(id);

COMMENT ON TABLE users IS 'Local authority and admin users.';
COMMENT ON COLUMN users.id IS 'PK auto incremented.';
COMMENT ON COLUMN users.email_address IS 'Email address.  Also Unique identifier of a user when logging in.';
COMMENT ON COLUMN users.forename IS 'Forename.';
COMMENT ON COLUMN users.surname IS 'Surname.';
COMMENT ON COLUMN users.role_id IS '1 role per user.  So FK directly to roles.';

--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS local_authority;
DROP TABLE IF EXISTS local_authority_area;
DROP TABLE IF EXISTS roles;
