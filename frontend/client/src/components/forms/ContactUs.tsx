"use client"

import { useCallback } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { z, ZodObject, ZodString } from "zod";
import { ErrorMessage } from "@hookform/error-message";
import { zodResolver } from "@hookform/resolvers/zod";
import { RoomList } from "@/model/type";
import { setData } from "@/api/setData";
import { CONTACT } from "@/constants/endpoints";

import './form.css'

type Labels = {
    name: string;
    email: string;
    phone: string
    room: string;
    comments: string;
    submit: string;
}

type Errors = {
    id: string;
    name: string;
    email: string;
    invalidEmail: string;
}

type Props = {
    labels: Labels;
    errors: Errors;
    rooms: RoomList[];
    language: "es" | "en"
}

type Contact = {
    id: ZodString;
    full_name: ZodString;
    email: ZodString;
    phone_number: ZodString;
    comment: ZodString;
}

const errorStyle = {
    border: "1px solid red",
    boxShadow: "0 0 5px rgba(178, 25, 25, 0.5)"
}

const getSchema : (errors : Errors) => ZodObject<Contact> = (errors) => {
    return z.object({
            id: z.string().nonempty(errors.id),
            full_name: z.string().nonempty(errors.name),
            email: z.string().nonempty(errors.email).email(errors.invalidEmail),
            phone_number: z.string(),
            comment: z.string()
    })
}


export default function ContactUs (props : Props) {
    const { labels, errors : labelsErrors, rooms, language } = props;
    const schema = getSchema(labelsErrors); 

    const { register, handleSubmit, setError, formState: {errors, isSubmitted }} = useForm<Contact>({
        defaultValues: {
            id: '',
            full_name: '',
            email: '',
            phone_number: '',
            comment: ''
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<Contact> = useCallback(async (data, event) => {
        event?.preventDefault();
        
        try {
            const res = await setData<Contact>(`${CONTACT}`, data, "POST", language);

            if(res.status >= 200 && res.status <= 299) {
                console.log("Ta wueno")
            }
            else {
                if(res.errors) {
                    Object.entries(res.errors).forEach(([key, value]) => {
                        setError(key as keyof Contact, { message: value });
                    })
                }
                else {
                    console.log("exploto esto xd")
                }
            }
        }
        catch (err: unknown){
            if(err instanceof Error) {
                console.log("exploto este bussness")
            }
        }
    }, [])

    return (
        <form>
            <div>
                <label htmlFor="full_name">{labels.name}</label>
                <input type="text" {...register('full_name')} style={errors.full_name && errorStyle}/>
                <span className="error-message"><ErrorMessage errors={errors} name="full_name"/></span>
            </div>
            <div>
                <label htmlFor="email">{labels.email}</label>
                <input type="email" {...register("email")} style={errors.email && errorStyle} />
                <span className="error-message"><ErrorMessage errors={errors} name="email"/></span>
            </div>
            <div>
                <label htmlFor="phone_number">{labels.phone}</label>
                <input type="number" {...register("phone_number")} style={errors.phone_number && errorStyle} />
                <span className="error-message"><ErrorMessage errors={errors} name="phone_number"/></span>
            </div>
            <div>
                <label htmlFor="id">{labels.room}</label>
                <select {...register("id")} style={errors.id && errorStyle}>
                    {rooms.map((room) => (
                        <option key={room.id} value={room.id}>{room.name}</option>
                    ))}
                </select>
                <span className="error-message"><ErrorMessage errors={errors} name="id"/></span>
            </div>
            <div>
                <label htmlFor="comment">{labels.comments}</label>
                <textarea {...register("comment")}/>
                <span className="error-message"><ErrorMessage errors={errors} name="comment"/></span>
            </div>
            <button 
                type="submit"
                onClick={(event) => handleSubmit(onSubmit)(event)}
            >
                {labels.submit}
            </button>
        </form>
    );
}