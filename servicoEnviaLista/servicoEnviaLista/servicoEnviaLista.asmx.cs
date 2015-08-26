using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace servicoEnviaLista
{
    /// <summary>
    /// Summary description for servicoEnviaLista
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/", Name = "servicoEnviaLista")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class servicoEnviaLista : System.Web.Services.WebService
    {

        /*[WebMethod]
        public string ValidacaoIdade(int Idade)
        {
            string mensagem = "pode entrar";
            if (Idade < 18)
                mensagem = "Não pode entrar";
            return mensagem;
        }*/
    }
}
