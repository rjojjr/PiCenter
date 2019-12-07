import React from "react";
import Button from 'react-bootstrap/Button';

/**
 * Generic tabbed page header. Meant to be used uniformly in a parent container.
 * @param onClickHandler
 * @param tabs
 * @param currentTabIndex
 * @param isLoading
 * @returns {*}
 * @constructor
 */
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
                        <Button
                            variant={"primary"}
                            key={index}
                            //disabled={() => isDisable(index)}
                            className={"tabSelector"}
                            onClick={() => {
                                onClickHandler(index);
                            }}
                        >
                            {tab}
                        </Button>
                    ))}
                </div>
            )}
        </div>
    );
};

export default GenericPageHeader;