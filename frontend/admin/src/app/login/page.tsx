"use client"

import { BaseSyntheticEvent, useCallback } from "react";

import { SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ErrorMessage } from "@hookform/error-message";
import { z } from "zod";

import { Card, CardContent, CardHeader, FormControl, FormHelperText, Paper, Stack, TextField } from "@mui/material";

import LoadingButton from '@mui/lab/LoadingButton';
import LoginIcon from '@mui/icons-material/Login';

import "./login.css"

const schema = z.object({
    email: z.string().email("Invalid email address").nonempty("Email is required"),
    password: z.string().min(1, "Password is required")
})

type Login = z.infer<typeof schema>

export default function LoginPage () {
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm<Login>({
        defaultValues: {
            email: '',
            password: ''
        },
        resolver: zodResolver(schema)
    })

    const onSubmit : SubmitHandler<Login> = useCallback(async (data, event?: BaseSyntheticEvent) => {
        event?.preventDefault();
        try {
            
        }
        catch (err) {
            console.log(err)
        }
    }, []);

    return (
        <Card className="body_card">
            <CardHeader 
                title="Login"
            />
            <CardContent>
                <form className="content">
                    <FormControl 
                        fullWidth
                        error={Boolean(errors)}
                    >
                        <TextField 
                            disabled={isSubmitting}
                            error={Boolean(errors.email)}
                            required
                            label='Email'
                            {...register("email")}
                        />
                        { errors.email &&
                            <FormHelperText>
                                <ErrorMessage errors={errors} name="email"/>
                            </FormHelperText>
                        }
                    </FormControl>
                    <FormControl 
                        fullWidth
                        error={Boolean(errors)}
                    >
                        <TextField 
                            disabled={isSubmitting}
                            error={Boolean(errors.password)}
                            required
                            label='Password'
                            type="password"
                            {...register("password")}
                        />
                        { errors.password &&
                            <FormHelperText>
                                <ErrorMessage errors={errors} name="password"/>
                            </FormHelperText>
                        }
                    </FormControl>
                    <Stack direction="row" justifyContent="end">
                        <LoadingButton
                            loading={isSubmitting}
                            variant='outlined' 
                            type='submit' 
                            onClick={(event) => handleSubmit(onSubmit)(event)}
                            endIcon={<LoginIcon />}
                        >
                            Login
                        </LoadingButton>
                    </Stack>
                </form>
            </CardContent>
        </Card>
    );
}