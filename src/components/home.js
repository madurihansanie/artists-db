import React, {Component} from 'react';
import Banner from '../components/banner';
import ArtistsList from './artistList';
const URL_ARTISTS= '  http://localhost:3004/artists';

class Home extends Component{
    
    constructor(props){
        super(props);
        this.state={
            artists:''
        }
    }
    componentDidMount(){
        fetch(URL_ARTISTS,{
            method:'GET'

        })
        .then(response=>response.json())
        .then(json=>{
            console.log(json)
            this.setState({
                artists:json
            })
        })
    }

    render(){
        return(
            <div>
                    <Banner></Banner>
                    <ArtistsList allArtists={this.state.artists}></ArtistsList>
            </div>
        )
    }

}
export default Home;