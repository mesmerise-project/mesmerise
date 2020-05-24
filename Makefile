.PHONY: app client build-env dist
VERSION := $(shell grep ^version server/build.gradle | sed -e "s/version '\\([^']\\+\\)'/\\1/g")
DISTDIR := build/fairytale-$(VERSION)

fairytale-$(VERSION).jar: app
	cp server/build/libs/fairytale-$(VERSION)-all.jar ./fairytale-$(VERSION).jar

build-env: client/node_modules
	mkdir -p server/src/main/resources/client

client/node_modules:
	cd client && npm install

client: build-env
	cd client && npm run build

dist: fairytale-$(VERSION).jar
	@mkdir -p $(DISTDIR)
	@rm -r $(DISTDIR)/* ; true
	cp fairytale-$(VERSION).jar $(DISTDIR)/fairytale.jar
	mkdir $(DISTDIR)/adventures
	cd build && zip -r ../fairytale-$(VERSION).zip fairytale-$(VERSION)

app: build-env client
	cp -r client/build/* server/src/main/resources/client/
	cd server && ./gradlew build