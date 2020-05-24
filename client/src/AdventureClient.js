class AdventureClient {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  async get(segments, jsonResponse = true) {
    const response = await fetch([this.baseUrl, ...segments].join('/'))
    return jsonResponse
      ? await response.json()
      : response.status;
  }

  async listAdventures() {
    return await this.get(["adventures"])
  }

  async loadAdventure(name) {
    return await this.get(["adventures", name])
  }

  async setScene(adventure, scene) {
    return await this.get(["adventures", adventure, "scenes", scene], false);
  }

  async setMuted(muted) {
    return await this.get([muted ? "silence" : "unsilence"], false)
  }

  sceneThumbnailUrl(adventure, scene) {
    return `${this.baseUrl}/adventures/${adventure}/scenes/${scene}/thumbnail`;
  }
}

export default AdventureClient;