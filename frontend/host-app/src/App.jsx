import React, { Suspense, useState, useEffect } from "react";
import ReactDOM from "react-dom";

const loadRemoteComponent = async (scope, module) => {
  console.log(`Attempting to load ${scope} - ${module}`);

  if (!window[scope]) {
    throw new Error(`Remote scope ${scope} is not available.`);
  }

  await window[scope].init(__webpack_share_scopes__.default);

  const factory = await window[scope].get(module);
  return factory();
};

const LazyComponent = ({ scope, module }) => {
  const [Component, setComponent] = useState(null);

  useEffect(() => {
    loadRemoteComponent(scope, module)
      .then((ComponentModule) => setComponent(() => ComponentModule.default))
      .catch((error) =>
        console.error(`🚨 Error loading ${module} from ${scope}`, error)
      );
  }, [scope, module]);

  if (!Component) return <div>Loading {module}...</div>;
  return <Component />;
};

const LazyStyles = ({ scope, module }) => {
  useEffect(() => {
    loadRemoteComponent(scope, module).catch((error) =>
      console.error(`Error loading ${module} from ${scope}`, error)
    );
  }, [scope, module]);

  return null;
};

const App = () => {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [render, setRender] = useState(false);
  const [view, setView] = useState("login");

  useEffect(() => {
    setRender(true);
  }, []);
  if (!render) return <div>Loading...</div>;

  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken(null);
  };

  return (
    <div className="relative flex flex-col items-center justify-center bg-gray-50 h-screen">
      {token && (
        <button
          onClick={handleLogout}
          className="absolute top-6 right-6 px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition shadow-md z-40"
        >
          Logout
        </button>
      )}

      <Suspense fallback={<div>Loading styles...</div>}>
        <LazyStyles scope="auth" module="./styles" />
      </Suspense>
      <Suspense fallback={<div>Loading styles...</div>}>
        <LazyStyles scope="interactions" module="./styles" />
      </Suspense>

      {token ? (
        <Suspense
          fallback={
            <div className="flex items-center justify-center mt-4">
              Loading...
            </div>
          }
        >
          <LazyComponent scope="interactions" module="./InteractionsLogic" />
        </Suspense>
      ) : (
        <Suspense
          fallback={
            <div className="flex items-center justify-center">
              Loading login...
            </div>
          }
        >
          <LazyComponent scope="auth" module="./AuthLogic" />
        </Suspense>
      )}
    </div>
  );
};

export default App;
