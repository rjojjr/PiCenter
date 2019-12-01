import React, {useState} from "react";
import Alert from "react-bootstrap/Alert";

const DownloadAlert = ({show, isDownload}) => {

    const [showAlert, setShowAlert] = useState(show);

    const close = () => {
        isDownload(false);
        setShowAlert(false);
    };

    return(
        <div className={"alert csvDownloadAlert"}>
            {showAlert && (
                <Alert variant="success" onClose={close} dismissible>
                    <Alert.Heading>Your download is ready!</Alert.Heading>
                   <p>
                       <a href="/download/backup">Click here to download</a>
                   </p>
                </Alert>
            )}
        </div>
    );
}
export default DownloadAlert