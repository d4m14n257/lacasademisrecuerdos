"use client"

import { z, ZodObject, ZodString } from 'zod';
import './form.css'
import { SubmitHandler, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useCallback } from 'react';
import { ErrorMessage } from '@hookform/error-message';

type Labels = {
    name: string;
    email: string;
    check_in: string;
    check_out: string;
    guests: {
        label: string;
        select: string[];
    };
    comment: string;
    submit: string;
}

type Errors = {
    name: string;
    email: string;
    invalidEmail: string;
}

type Props = {
    id: string
    labels: Labels;
    errors: Errors;
}

type Contact = {
    id: ZodString;
    full_name: ZodString;
    email: ZodString;
    check_in: ZodString;
    check_out: ZodString;
    guests: ZodString;
    comment: ZodString;
}

const getSchema : (errors : Errors) => ZodObject<Contact> = (errors) => {
    return z.object({
            id: z.string().nonempty(),
            full_name: z.string().nonempty(errors.name),
            email: z.string().nonempty(errors.email).email(errors.invalidEmail),
            check_in: z.string().date(),
            check_out: z.string().date(),
            guests: z.string(),
            comment: z.string()
    })
}

const errorStyle = {
    border: "1px solid red",
    boxShadow: "0 0 5px rgba(178, 25, 25, 0.5)"
}

export default function Reservation (props : Props) {
    const { id, labels, errors : labelsErrors } = props;
    const schema = getSchema(labelsErrors);

    const { register, handleSubmit, setError, formState: {errors, isSubmitted }} = useForm<Contact>({
        defaultValues: {
            id: id,
            full_name: '',
            email: '',
            check_in: '',
            check_out: '',
            guests: '',
            comment: ''
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<Contact> = useCallback(async (data, event) => {
        event?.preventDefault();

        try {
            console.log(data)
        }
        catch (err : unknown) {
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
                <label htmlFor="check_in">{labels.check_in}</label>
                <input type="date" {...register("check_in")} style={errors.check_in && errorStyle}/>
                <span className="error-message"><ErrorMessage errors={errors} name="check_in"/></span>
            </div>
            <div>
                <label htmlFor="check_out">{labels.check_in}</label>
                <input type="date" {...register("check_out")} style={errors.check_out && errorStyle}/>
                <span className="error-message"><ErrorMessage errors={errors} name="check_out"/></span>
            </div>
            <div>
                <label htmlFor="guests">{labels.guests.label}</label>
                <select {...register("guests")} style={errors.guests && errorStyle}>
                    {labels.guests.select.map((item) => (
                        <option key={item} value={item}>{item}</option>
                    ))}
                </select>
                <span className="error-message"><ErrorMessage errors={errors} name="guests"/></span>
            </div>
            <div>
                <label htmlFor="comment">{labels.comment}</label>
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