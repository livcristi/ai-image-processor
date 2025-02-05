export const loadRemoteComponent = (scope, module) => {
  return async () => {
    console.log(`Attempting to load remote module: ${scope} -> ${module}`);

    while (!window[scope]) {
      console.warn(`Waiting for remote ${scope} to be available...`);
      await new Promise((resolve) => setTimeout(resolve, 500));
    }

    await __webpack_init_sharing__("default");
    const container = window[scope];

    if (!container) {
      throw new Error(`Container for scope ${scope} is not available.`);
    }

    await container.init(__webpack_share_scopes__.default);
    const factory = await container.get(module);
    return factory();
  };
};
