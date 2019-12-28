import React from 'react';
import ReactLoading from 'react-loading';
import ReactModal from 'react-modal';

const LoadingView = ({ isLoading }) => {
  const customModalStyles = {
    content: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      position: 'static',
      background: 'none',
      border: 'none',
      boxShadow: 'none',
    }
  };
  return (
    <ReactModal
      isOpen={isLoading}
      style={customModalStyles}
      ariaHideApp={false}
    >
      <ReactLoading
        className="loadingBubbles"
        type={'spinningBubbles'}
        color={'#1976D2'}
        height={'10rem'}
        width={'10rem'}
      />
    </ReactModal>
  );
};

export default LoadingView;
