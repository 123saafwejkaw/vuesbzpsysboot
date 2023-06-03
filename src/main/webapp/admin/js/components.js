var datatable=Vue.extend({
    name:'data-table',
    props:{theader:{},tdata:{}},
    template:[
        ' <table class="data-table data-table-feature" role="grid"' +
        '                                  style="width: 894px;">' +
        '             <thead>' +
        '                 <tr role="row">' +
        '                     <th v-for="name in theader" >{{name}}</th>' +
        '                     <th  >操作</th>' +
        '                 </tr>' +
        '             </thead>' +
        '             <tbody>' +
        '       ' +
        '                 <tr role="row" class="odd" v-for="item in tdata">' +
        '                     <td tabindex="0" v-for="value in item ">' +
        '                          <p class="list-item-heading" >{{value}}</p>' +
        '                     </td>' +
        '                     <td>' +
        '                               <div class="btn-group" role="group" aria-label="Basic example">\n' +
        '                                    <button type="button"  @click="tedit(item.id)" class="btn  btn-primary">编辑</button>\n' +
        '                                    <button type="button"  @click="tdelete(item.id)" class="btn  btn-primary">删除</button>\n' +
        '                                </div>' +
        '                     </td>' +
        '                 </tr>' +
        '             </tbody>' +
        '       </table>'
    ].join(),
    methods:{
        tedit(data){
            this.$emit('tedit',data);
        },
        tdelete(data){
            this.$emit('tdelete',data);
        },
    }
})


Vue.component('data-table',datatable);

