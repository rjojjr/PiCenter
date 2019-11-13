import React from "react";

const LoadingView = ({ isLoading , message }) => {
  return <div>{isLoading && <p className={"message"}>{message}</p>}</div>;
};
export default LoadingView;
