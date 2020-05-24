.PHONY: app
VERSION := $(shell grep ^version server/build.gradle | sed -e "s/version '\\([^']\\+\\)'/\\1/g")

fairytale-$(VERSION).jar: app
	cp server/build/libs/fairytale-$(VERSION)-all.jar ./fairytale-$(VERSION).jar

app:
	cd client && npm run build
	cp -r client/build/* server/src/main/resources/client/
	cd server && ./gradlew build