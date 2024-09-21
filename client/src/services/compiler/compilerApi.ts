import axiosInstance from '../axiosInstance';

export const uploadCode = async(code: string): Promise<string> => {
    try {
        const response = await axiosInstance.post<string>('/compiler/upload', code, {
            headers: {
                'Content-Type': 'text/plain'
            }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
}

export default {uploadCode};