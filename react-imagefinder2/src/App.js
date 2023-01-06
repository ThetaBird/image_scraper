import React from "react";
const axios = require('axios');

//import { ImageList } from "./Components/ImageList";

export default class App extends React.Component{
    state = {
        submitText:"Submit URL",
        url:"",
        images:true
    }

    handleURL = (e) => {
        //Update value of input field without rerendering any components
        this.state[e.target.name] = e.target.value;
    }

    handleSubmit = async (e) => {
        e.preventDefault();

        //Disable button and update text while waiting for response from servlet
        let submitText = "Submitting...";
        this.setState({submitText});

        //Send POST request to servlet
        let url = `http://localhost:8080/main?url=${this.state.url}`;
        console.log(url);
        const result = await axios.post(url).catch(error => console.log(error))
        
        //After response received, unlock button and reset text
        submitText = "Submit URL";

        //If response exists, update state with data. If not, update with FALSE so that an error message renders to the user
        let images = false;
        if(result){
            images = result.data;   
        }
        this.setState({submitText, images});
        
        
    }

    render(){
        return (
            <div className="test">
                <div className="text-custom px-5 py-4">
                    <div className="display-4">Rudimentary Multi-Threaded Image Scraper</div>
                    <div className="h6 text-muted p-1">Submission by ThetaBird</div>
                </div>
                <div className="input-group px-5">
                    <div className="input-group-prepend">
                        <button disabled={this.state.submitText == "Submitting..."} className="btn btn-outline-secondary" type="button" onClick={this.handleSubmit}>{this.state.submitText}</button>
                    </div>
                    <input className="form-control input-custom" onChange={this.handleURL} type="text" name="url"></input> 
                </div>
                
                {this.state.images ?
                
                <div> 
                    {this.state.images!=true ? 
                        <div>
                            <div className="px-5 py-2 h6 text-light">{`${this.state.images.length} image(s)`}</div>
                            <div className="imageList p-5">
                                {this.state.images.map((imageUrl) => <img height="200" src={imageUrl} onerror="this.style.display='none'"/>)}
                            </div>
                        </div>
                        : <div/>
                    }
                    
                </div>  
                :
                <div className="text-danger mx-2 px-5 py-2">Something went wrong! Please make sure that your url is valid, or check console.</div>
                }
                  
            </div>
        );
    }
}
