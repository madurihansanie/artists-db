import React from 'react';

const Painting=(props)=>{
    console.log(props);
    const showList=({albumList})=>{
        if(albumList){
            return albumList.map((item,index)=>{
                return(
        <img key={index}
         src={`/images/albums/${item.cover}.jpg`}></img>
                )
            })
        }
    }
    return(
       
        <div className="albums_list">
        {showList(props)}
             
        </div>
    )
}
export default Painting;