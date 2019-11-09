import React from "react";
import "./App.css";
import InitialAppLoader from "./comps/InitialAppLoader";
import { Provider } from "react-redux";
import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import mainReducer from "./reducers/main-reducer";

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(
  mainReducer,
  composeEnhancers(mainReducer, applyMiddleware(thunk))
);

function App() {
  return (
      <Provider store={store}>
        <InitialAppLoader />
      </Provider>
  );
}

export default App;
