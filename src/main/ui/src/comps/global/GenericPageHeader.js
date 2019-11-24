import React from "react";

const GenericPageHeader = ({ onClickHandler, tabs, currentTabIndex, isLoading }) => {

    const isDisable = (index) => {
        return index === currentTabIndex
    }

    return (
        <div className={"genericHeader headerButtonContainer"}>
            <h3 >{tabs[currentTabIndex]}</h3>
            {!isLoading && (

                <div>

                    {tabs.map((tab, index) => (
                        <button
                            key={index}
                            disabled={() => isDisable(index)}
                            className={"tabSelector"}
                            onClick={() => {
                                onClickHandler(index);
                            }}
                        >
                            {tab}
                        </button>
                    ))}
                </div>
            )}
        </div>
    );
};

export default GenericPageHeader;