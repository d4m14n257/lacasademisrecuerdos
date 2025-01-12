"use client"

import { CircularProgress } from "@mui/material";
import { GoogleMap, LoadScript, useJsApiLoader } from "@react-google-maps/api";
import { useCallback, useState } from "react";

const center = {
    lat: -3.745,
    lng: -38.523
};

export default function Map () {
    const API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY ? process.env.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY : "";
    const [map, setMap] = useState(null);

    const { isLoaded } = useJsApiLoader({
        id: 'google-map-script',
        googleMapsApiKey: API_KEY
    })

    const onLoad = useCallback(
        function callback(map : any) {
            const bounds = new window.google.maps.LatLngBounds(center);
            map.fitBounds(bounds);
        
            setMap(map)
      }, [])

    const onUnmount = useCallback(function callback(map : any) {
        setMap(null)
    }, [])
    
    return isLoaded ? (
        <GoogleMap
            center={center}
            zoom={10}
            onLoad={onLoad}
            onUnmount={onUnmount}
        >
        </GoogleMap>
    ) : <></>
        
}