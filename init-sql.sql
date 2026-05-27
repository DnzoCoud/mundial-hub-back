CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name TEXT NOT NULL,
   email TEXT UNIQUE NOT NULL,
   password TEXT NOT NULL,
   role TEXT NOT NULL,
   status TEXT NOT NULL,
   created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE user_profile (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    full_name TEXT,
    birth_date DATE,
    city TEXT,
    country TEXT,
    avatar_url TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE preference (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    category TEXT NOT NULL,
    value TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_pref_user ON preference(user_id);

CREATE TABLE "group" (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     name TEXT,
     created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE user_group (
    user_id UUID,
    group_id UUID,
    role TEXT NOT NULL,
    joined_at TIMESTAMPTZ DEFAULT now(),
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES "group"(id) ON DELETE CASCADE,
    UNIQUE(user_id, group_id)
);

CREATE TABLE pool (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      group_id UUID,
      name TEXT NOT NULL,
      description TEXT,
      code TEXT UNIQUE NOT NULL,
      max_members INT,
      is_private BOOLEAN DEFAULT false,
      status TEXT NOT NULL DEFAULT 'ACTIVE',
      starts_at TIMESTAMPTZ,
      ends_at TIMESTAMPTZ,
      FOREIGN KEY (group_id) REFERENCES "group"(id)
);

CREATE TABLE user_pool (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     pool_id UUID NOT NULL,
     user_id UUID NOT NULL,
     role TEXT NOT NULL,
     joined_at TIMESTAMPTZ DEFAULT now(),

     FOREIGN KEY (pool_id) REFERENCES pool(id) ON DELETE CASCADE,
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
     UNIQUE(pool_id, user_id)
);

CREATE TABLE ranking (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     position INT,
     pool_id UUID,
     FOREIGN KEY (pool_id) REFERENCES pool(id)
);

CREATE TABLE team (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      name TEXT NOT NULL
);

CREATE TABLE player (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    team_id UUID,
    FOREIGN KEY (team_id) REFERENCES team(id)
);

CREATE INDEX idx_player_team ON player(team_id);

CREATE TABLE stadium (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     name TEXT NOT NULL
);

CREATE TABLE match (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   home_team UUID NOT NULL,
   away_team UUID NOT NULL,
   date_time TIMESTAMPTZ,
   status TEXT,
   result TEXT,
   stadium_id UUID,
   FOREIGN KEY (home_team) REFERENCES team(id),
   FOREIGN KEY (away_team) REFERENCES team(id),
   FOREIGN KEY (stadium_id) REFERENCES stadium(id)
);

CREATE INDEX idx_match_date ON match(date_time);

CREATE TABLE event (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   event_type TEXT,
   event_date_time TIMESTAMPTZ
);

CREATE TABLE match_event (
     event_id UUID,
     match_id UUID,
     PRIMARY KEY (event_id, match_id),
     FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
     FOREIGN KEY (match_id) REFERENCES match(id) ON DELETE CASCADE
);

CREATE TABLE prediction (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    predicted_score TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    user_id UUID NOT NULL,
    match_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (match_id) REFERENCES match(id)
);

CREATE INDEX idx_prediction_user ON prediction(user_id);
CREATE INDEX idx_prediction_match ON prediction(match_id);

CREATE TABLE sticker_pack (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  size INT
);
CREATE TABLE sticker (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     category TEXT,
     player_id UUID,
     FOREIGN KEY (player_id) REFERENCES player(id)
);

CREATE TABLE pack_sticker (
  pack_id UUID,
  sticker_id UUID,
  PRIMARY KEY (pack_id, sticker_id),
  FOREIGN KEY (pack_id) REFERENCES sticker_pack(id) ON DELETE CASCADE,
  FOREIGN KEY (sticker_id) REFERENCES sticker(id)
);

CREATE TABLE album (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   progress INT DEFAULT 0,
   user_id UUID NOT NULL,
   FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE album_sticker (
   album_id UUID,
   sticker_id UUID,
   PRIMARY KEY (album_id, sticker_id),
   FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,
   FOREIGN KEY (sticker_id) REFERENCES sticker(id)
);

CREATE TABLE trade (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   status TEXT,
   user_id UUID,
   FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE trade_sticker (
   trade_id UUID,
   sticker_id UUID,
   PRIMARY KEY (trade_id, sticker_id),
   FOREIGN KEY (trade_id) REFERENCES trade(id) ON DELETE CASCADE,
   FOREIGN KEY (sticker_id) REFERENCES sticker(id)
);

CREATE TABLE trade_request (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   status TEXT,
   trade_id UUID,
   FOREIGN KEY (trade_id) REFERENCES trade(id)
);

CREATE TABLE score (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   value INT,
   user_id UUID,
   FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE reservation (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     status TEXT,
     created_at TIMESTAMPTZ DEFAULT now(),
     expires_at TIMESTAMPTZ,
     user_id UUID,
     FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE ticket (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    price NUMERIC,
    status TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    reservation_id UUID,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);

CREATE TABLE payment (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     amount NUMERIC,
     status TEXT,
     provider TEXT,
     paid_at TIMESTAMPTZ,
     sandbox_reference TEXT,
     reservation_id UUID,
     FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);

CREATE TABLE transfer (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      requested_at TIMESTAMPTZ,
      confirmed_at TIMESTAMPTZ,
      user_id UUID,
      ticket_id UUID,
      FOREIGN KEY (user_id) REFERENCES users(id),
      FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);

CREATE TABLE refund (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC,
    status TEXT,
    reason TEXT,
    requested_at TIMESTAMPTZ,
    ticket_id UUID,
    user_id UUID,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE personal_agenda (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     last_sync TIMESTAMPTZ,
     user_id UUID,
     FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE agenda_entry (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      status TEXT,
      added_at TIMESTAMPTZ,
      agenda_id UUID,
      FOREIGN KEY (agenda_id) REFERENCES personal_agenda(id)
);

CREATE TABLE reminder (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      category TEXT,
      value TEXT,
      entity_id UUID
);

CREATE TABLE support_agent (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name TEXT
);

CREATE TABLE support_case (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  status TEXT,
  description TEXT,
  urgency TEXT,
  created_at TIMESTAMPTZ,
  closed_at TIMESTAMPTZ,
  user_id UUID,
  agent_id UUID,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (agent_id) REFERENCES support_agent(id)
);

CREATE TABLE evidence (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  file_name TEXT,
  type TEXT,
  size_mb INT,
  format TEXT,
  case_id UUID,
  FOREIGN KEY (case_id) REFERENCES support_case(id)
);

CREATE TABLE fraud_rule (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type TEXT,
    threshold NUMERIC,
    active BOOLEAN
);

CREATE TABLE account_block (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   reason TEXT,
   blocked_at TIMESTAMPTZ,
   released_at TIMESTAMPTZ,
   fraud_rule_id UUID,
   user_id UUID,
   FOREIGN KEY (fraud_rule_id) REFERENCES fraud_rule(id),
   FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE auditable_event (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     type_observability TEXT,
     payment_id UUID,
     transfer_id UUID,
     refund_id UUID,
     reservation_id UUID,
     FOREIGN KEY (payment_id) REFERENCES payment(id),
     FOREIGN KEY (transfer_id) REFERENCES transfer(id),
     FOREIGN KEY (refund_id) REFERENCES refund(id),
     FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);

CREATE TYPE user_role AS ENUM (
    'FAN',
    'OPERATOR',
    'SUPPORT',
    'COMPLIANCE',
    'ADMIN'
);

CREATE TYPE group_role AS ENUM (
    'OWNER',
    'ADMIN',
    'MEMBER'
);

CREATE TYPE pool_role AS ENUM (
    'ADMIN',
    'PLAYER'
);

ALTER TABLE users
ALTER COLUMN role TYPE user_role
USING role::user_role;

ALTER TABLE user_group
ALTER COLUMN role TYPE group_role
USING role::group_role;

ALTER TABLE user_pool
ALTER COLUMN role TYPE pool_role
USING role::pool_role;

ALTER TABLE "group"
    ADD COLUMN invite_token VARCHAR(100) NULL;

ALTER TABLE "group"
    ADD CONSTRAINT uq_invite_token UNIQUE (invite_token);

ALTER TABLE "group"
    RENAME TO groups;

ALTER TABLE user_group
    DROP CONSTRAINT "user_group_pkey";

ALTER TABLE user_group
    ADD COLUMN id UUID DEFAULT gen_random_uuid() PRIMARY KEY;