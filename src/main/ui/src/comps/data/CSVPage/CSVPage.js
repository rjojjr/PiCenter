import React from "react";
import Button from "react-bootstrap/Button";

const CSVPage = ({user, isDataError, getCSV}) => {
    
    const handleOnClick = (e) => {
        //isDataError(false, '');
        e.preventDefault();
        getCSV(user);
    }
    
    return(
        <div className={"page csvPage"}>
            <form>
                <table>
                    <tbody>
                    <tr>
                        <td>
                            Download readings CSV?
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <Button variant={"primary"} onClick={handleOnClick}>
                                Download
                            </Button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    )
    
}
export default CSVPage