set -e

# 환경 변수에서 기본 DB 정보 로드
DB_NAME="$POSTGRES_DB"
DB_USER="$POSTGRES_USER"

echo "Initializing PostgreSQL schemas for QuickBaesong MSA..."

# PostgreSQL에 접속하여 스키마 생성 및 권한 부여
# ${DB_NAME} (quickbaesong_main) 데이터베이스에 접속하여 SQL 실행
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$DB_NAME" <<-EOSQL

    -- Service용 스키마 생성 및 권한 부여
    CREATE SCHEMA IF NOT EXISTS order_schema AUTHORIZATION $DB_USER;
    CREATE SCHEMA IF NOT EXISTS user_schema AUTHORIZATION $DB_USER;
    CREATE SCHEMA IF NOT EXISTS item_schema AUTHORIZATION $DB_USER;
    CREATE SCHEMA IF NOT EXISTS company_schema AUTHORIZATION $DB_USER;
    CREATE SCHEMA IF NOT EXISTS hub_schema AUTHORIZATION $DB_USER;
    CREATE SCHEMA IF NOT EXISTS notification_schema AUTHORIZATION $DB_USER;
    CREATE SCHEMA IF NOT EXISTS delivery_schema AUTHORIZATION $DB_USER;

    -- 생성된 스키마에 대한 권한을 DB 사용자에게 부여
    GRANT ALL ON SCHEMA order_schema TO $DB_USER;
    GRANT ALL ON SCHEMA user_schema TO $DB_USER;
    GRANT ALL ON SCHEMA item_schema TO $DB_USER;
    GRANT ALL ON SCHEMA company_schema TO $DB_USER;
    GRANT ALL ON SCHEMA hub_schema TO $DB_USER;
    GRANT ALL ON SCHEMA notification_schema TO $DB_USER;
    GRANT ALL ON SCHEMA delivery_schema TO $DB_USER;

    SELECT 'All schemas created and permissions granted successfully' AS status;

EOSQL

echo "Initialization complete."