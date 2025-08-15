# ApexBlog - 杞婚噺鍖栦釜浜哄崥瀹㈢郴缁�

## 椤圭洰绠€浠�

ApexBlog 鏄竴涓熀浜� Spring Boot 3.x 寮€鍙戠殑杞婚噺鍖栦釜浜哄崥瀹㈢郴缁燂紝閲囩敤鍓嶅悗绔垎绂绘灦鏋勶紝杩芥眰鏋佽嚧鐨勬€ц兘鍜岀畝娲佺殑璁捐銆�

## 鎶€鏈爤

- **鍚庣妗嗘灦**: Spring Boot 3.2.0
- **Java鐗堟湰**: Java 17
- **鏋勫缓宸ュ叿**: Gradle 8.5
- **鏁版嵁搴�**: MySQL 8.0
- **缂撳瓨**: Redis 7.x
- **瀹夊叏璁よ瘉**: Spring Security + JWT
- **API鏂囨。**: SpringDoc OpenAPI 3
- **瀹瑰櫒鍖�**: Docker + Docker Compose

## 涓昏鍔熻兘

### 鍓嶅彴鍔熻兘
- 鉁� 鏂囩珷娴忚锛堟敮鎸丮arkdown娓叉煋锛�
- 鉁� 鏂囩珷鎼滅储锛堝叏鏂囨悳绱級
- 鉁� 鍒嗙被鍜屾爣绛炬祻瑙�
- 鉁� 浣滃搧闆嗗睍绀�
- 鉁� 鍙嬮摼灞曠ず
- 鉁� RSS璁㈤槄
- 鉁� 閭璁㈤槄
- 鉁� 璁块棶缁熻

### 鍚庡彴鍔熻兘
- 鉁� 鏂囩珷绠＄悊锛圕RUD銆佸彂甯冦€佸綊妗ｏ級
- 鉁� 鍒嗙被鍜屾爣绛剧鐞�
- 鉁� 浣滃搧闆嗙鐞�
- 鉁� 鍙嬮摼绠＄悊
- 鉁� 閭璁㈤槄绠＄悊
- 鉁� 璁块棶缁熻鏌ョ湅
- 鉁� 鐢ㄦ埛璁よ瘉锛堜粎闄愬崥涓荤櫥褰曪級

### 鎶€鏈壒鎬�
- 鉁� Redis缂撳瓨浼樺寲
- 鉁� 寮傛浠诲姟澶勭悊
- 鉁� 璁块棶缁熻锛圛P鍘婚噸锛�
- 鉁� 閭欢閫氱煡
- 鉁� 鍏ㄦ枃鎼滅储
- 鉁� RESTful API
- 鉁� Docker閮ㄧ讲
- 鉁� 鎬ц兘鐩戞帶

## 蹇€熷紑濮�

### 鐜瑕佹眰

- Java 17+
- MySQL 8.0+
- Redis 7.x+
- Docker & Docker Compose锛堝彲閫夛級

### 鏈湴寮€鍙�

1. **鍏嬮殕椤圭洰**
```bash
git clone https://github.com/xuyi/ApexBlog.git
cd ApexBlog
```

2. **閰嶇疆鏁版嵁搴�**
```sql
CREATE DATABASE apex_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **閰嶇疆鐜鍙橀噺**
```bash
cp .env.example .env
# 缂栬緫 .env 鏂囦欢锛岄厤缃暟鎹簱鍜岄偖浠剁瓑淇℃伅
```

4. **杩愯搴旂敤**
```bash
./gradlew bootRun
```

5. **璁块棶搴旂敤**
- API鏂囨。: http://localhost:8888/api/swagger-ui.html
- 鍋ュ悍妫€鏌�: http://localhost:8888/api/actuator/health

### Docker閮ㄧ讲

1. **浣跨敤閮ㄧ讲鑴氭湰**
```bash
# 鍚姩鎵€鏈夋湇鍔�
./deploy.sh start

# 鍚姩鏈嶅姟骞跺寘鍚玁ginx
./deploy.sh start --with-nginx

# 鏌ョ湅鏈嶅姟鐘舵€�
./deploy.sh status

# 鏌ョ湅鏃ュ織
./deploy.sh logs

# 鍋滄鏈嶅姟
./deploy.sh stop
```

2. **鎵嬪姩閮ㄧ讲**
```bash
# 澶嶅埗鐜鍙橀噺鏂囦欢
cp .env.example .env

# 鍚姩鏈嶅姟
docker-compose up -d

# 鏌ョ湅鏃ュ織
docker-compose logs -f
```

## API鏂囨。

鍚姩搴旂敤鍚庯紝璁块棶 [Swagger UI](http://localhost:8888/api/swagger-ui.html) 鏌ョ湅瀹屾暣鐨凙PI鏂囨。銆�

### 涓昏鎺ュ彛

#### 鍓嶅彴鎺ュ彛
- `GET /articles` - 鑾峰彇鏂囩珷鍒楄〃
- `GET /articles/{id}` - 鑾峰彇鏂囩珷璇︽儏
- `GET /articles/search` - 鎼滅储鏂囩珷
- `GET /categories` - 鑾峰彇鍒嗙被鍒楄〃
- `GET /tags` - 鑾峰彇鏍囩鍒楄〃
- `GET /portfolios` - 鑾峰彇浣滃搧闆�
- `GET /friend-links` - 鑾峰彇鍙嬮摼
- `GET /rss/feed.xml` - RSS璁㈤槄
- `POST /email-subscribers/subscribe` - 閭璁㈤槄

#### 鍚庡彴鎺ュ彛锛堥渶瑕佽璇侊級
- `POST /auth/login` - 鐢ㄦ埛鐧诲綍
- `POST /admin/articles` - 鍒涘缓鏂囩珷
- `PUT /admin/articles/{id}` - 鏇存柊鏂囩珷
- `DELETE /admin/articles/{id}` - 鍒犻櫎鏂囩珷

## 閰嶇疆璇存槑

### 搴旂敤閰嶇疆

涓昏閰嶇疆鏂囦欢浣嶄簬 `src/main/resources/application.yml`锛�

```yaml
server:
  port: 8888  # 搴旂敤绔彛

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/apex_blog
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
  
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}

jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 86400000  # 24灏忔椂

blog:
  title: "Xuyi's Blog"
  description: "涓汉鎶€鏈崥瀹�"
  author: "xuyi"
  url: ${BLOG_URL:http://localhost:8888}
```

### 鐜鍙橀噺

```bash
# 鏁版嵁搴撻厤缃�
DB_USERNAME=apex_user
DB_PASSWORD=your_password

# Redis閰嶇疆
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT閰嶇疆
JWT_SECRET=your-jwt-secret

# 閭欢閰嶇疆
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# 鍗氬閰嶇疆
BLOG_URL=https://yourdomain.com
```

## 鎬ц兘浼樺寲

### 缂撳瓨绛栫暐
- 鏂囩珷鍒楄〃缂撳瓨锛�2灏忔椂
- 鏂囩珷璇︽儏缂撳瓨锛�2灏忔椂
- 鍒嗙被鏍囩缂撳瓨锛�6灏忔椂
- 鎼滅储缁撴灉缂撳瓨锛�30鍒嗛挓
- 璁块棶缁熻缂撳瓨锛�10鍒嗛挓

### 鏁版嵁搴撲紭鍖�
- 浣跨敤绱㈠紩浼樺寲鏌ヨ
- 杩炴帴姹犻厤缃�
- 鎵归噺鎿嶄綔浼樺寲
- 鍒嗛〉鏌ヨ

### 寮傛澶勭悊
- 閭欢鍙戦€佸紓姝ュ寲
- 璁块棶缁熻寮傛璁板綍
- 缂撳瓨棰勭儹

## 閮ㄧ讲寤鸿

### 鐢熶骇鐜
1. 浣跨敤澶栭儴MySQL鍜孯edis鏈嶅姟
2. 閰嶇疆SSL璇佷功
3. 浣跨敤Nginx鍙嶅悜浠ｇ悊
4. 閰嶇疆鏃ュ織鏀堕泦
5. 璁剧疆鐩戞帶鍛婅

### 瀹夊叏閰嶇疆
1. 淇敼榛樿绔彛
2. 閰嶇疆寮哄瘑鐮�
3. 鍚敤HTTPS
4. 閰嶇疆闃茬伀澧�
5. 瀹氭湡澶囦唤鏁版嵁

## 寮€鍙戞寚鍗�

### 椤圭洰缁撴瀯
```
src/main/java/com/xuyi/blog/
鈹溾攢鈹€ config/          # 閰嶇疆绫�
鈹溾攢鈹€ controller/      # 鎺у埗鍣�
鈹溾攢鈹€ dto/            # 鏁版嵁浼犺緭瀵硅薄
鈹溾攢鈹€ entity/         # 瀹炰綋绫�
鈹溾攢鈹€ exception/      # 寮傚父澶勭悊
鈹溾攢鈹€ repository/     # 鏁版嵁璁块棶灞�
鈹溾攢鈹€ security/       # 瀹夊叏閰嶇疆
鈹溾攢鈹€ service/        # 涓氬姟閫昏緫灞�
鈹斺攢鈹€ util/           # 宸ュ叿绫�
```

### 寮€鍙戣鑼�
1. 閬靛惊RESTful API璁捐
2. 浣跨敤缁熶竴鐨勫搷搴旀牸寮�
3. 瀹屽杽鐨勫紓甯稿鐞�
4. 璇︾粏鐨凙PI鏂囨。
5. 鍗曞厓娴嬭瘯瑕嗙洊

### 娴嬭瘯
```bash
# 杩愯鎵€鏈夋祴璇�
./gradlew test

# 杩愯鐗瑰畾娴嬭瘯
./gradlew test --tests ArticleServiceTest

# 鐢熸垚娴嬭瘯鎶ュ憡
./gradlew jacocoTestReport
```

## 璐＄尞鎸囧崡

1. Fork 椤圭洰
2. 鍒涘缓鐗规€у垎鏀� (`git checkout -b feature/AmazingFeature`)
3. 鎻愪氦鏇存敼 (`git commit -m 'Add some AmazingFeature'`)
4. 鎺ㄩ€佸埌鍒嗘敮 (`git push origin feature/AmazingFeature`)
5. 鎵撳紑 Pull Request

## 璁稿彲璇�

鏈」鐩噰鐢� MIT 璁稿彲璇� - 鏌ョ湅 [LICENSE](LICENSE) 鏂囦欢浜嗚В璇︽儏銆�

## 鑱旂郴鏂瑰紡

- 浣滆€�: xuyi
- 閭: xuyi@example.com
- 鍗氬: https://yourblog.com

## 鏇存柊鏃ュ織

### v1.0.0 (2024-01-01)
- 鍒濆鐗堟湰鍙戝竷
- 瀹屾垚鍩虹鍔熻兘寮€鍙�
- 鏀寔Docker閮ㄧ讲
- 瀹屽杽API鏂囨。

---

濡傛灉杩欎釜椤圭洰瀵逛綘鏈夊府鍔╋紝璇风粰涓� 猸愶笍 鏀寔涓€涓嬶紒
