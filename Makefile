.PHONY: app client build-env
VERSION := $(shell grep ^version server/build.gradle | sed -e "s/version '\\([^']\\+\\)'/\\1/g")

fairytale-$(VERSION).jar: app
	cp server/build/libs/fairytale-$(VERSION)-all.jar ./fairytale-$(VERSION).jar

build-env: client/node_modules
	mkdir -p server/src/main/resources/client

client/node_modules:
	cd client && npm install

client: build-env
	cd client && npm run build

app: build-env client
	cp -r client/build/* server/src/main/resources/client/
	cd server && ./gradlew build