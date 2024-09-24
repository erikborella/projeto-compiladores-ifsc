import axiosInstance from '../axiosInstance';

const uploadCode = async(code: string): Promise<string> => {
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

const getLlvmIrCode = async (codeId: string): Promise<string> => {
    try {
        const response = await axiosInstance.get<string>(`/compiler/${codeId}/llvm/ir`);

        return response.data;
    } catch (error) {
        throw error;
    }
}


export default {uploadCode, getLlvmIrCode};