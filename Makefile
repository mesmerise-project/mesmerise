.PHONY: app dist-app client copy-client build-env dist
VERSION := $(shell grep ^version server/build.gradle | sed -e "s/version '\\([^']*\\)'/\\1/g")
DISTDIR := build/mesmerise-$(VERSION)

mesmerise-$(VERSION).jar: app
	cp server/build/libs/mesmerise-$(VERSION)-all.jar ./mesmerise-$(VERSION).jar

build-env: client/node_modules
	mkdir -p server/src/main/resources/client

client/node_modules:
	cd client && npm ci

client: build-env
	cd client && npm run build

dist: dist-app
	@mkdir -p $(DISTDIR)
	@rm -r $(DISTDIR)/* ; true
	cp server/build/libs/mesmerise-$(VERSION)-min.jar $(DISTDIR)/mesmerise.jar
	mkdir $(DISTDIR)/adventures
	cd build && zip -r ../mesmerise-$(VERSION).zip mesmerise-$(VERSION)

copy-client: build-env client
	cp -r client/build/* server/src/main/resources/client/

app: copy-client
	cd server && ./gradlew build

dist-app: copy-client
	cd server && ./gradlew clean minimizedJar